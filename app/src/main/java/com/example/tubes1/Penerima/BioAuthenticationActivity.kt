package com.example.tubes1.Penerima


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.tubes1.databinding.ActivityBioAuthenticationBinding
import java.util.concurrent.Executor

class BioAuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBioAuthenticationBinding
    private lateinit var msg: String

    private lateinit var exec: Executor
    private lateinit var bioPrompt: BiometricPrompt
    private lateinit var fPrompt: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBioAuthenticationBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.btnAuth.isEnabled = false
        binding.fingerPrintIndicator.setOnClickListener{
            checkHardwareFingerPrint()
        }

        exec = ContextCompat.getMainExecutor(this)
        bioPrompt = BiometricPrompt(this, exec, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Authentication Succeeded", Toast.LENGTH_SHORT).show()
                createNewDataPenerima()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        })

        fPrompt = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verifikasi FingerPrint")
            .setSubtitle("Verifikasi untuk masuk ke menu penambahan penerima")
            .setNegativeButtonText("CANCEL")
            .build()

        binding.btnAuth.setOnClickListener {
            bioPrompt.authenticate(fPrompt)
        }
    }

    private fun checkHardwareFingerPrint(){
        val bioManager = BiometricManager.from(this)
        when(bioManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("APP_AUTH", "Aplikasi Dapat Melakukan Verifikasi BioMetrics")
                msg = "Verifikasi BioMetrics dapat Dilakukan"
                binding.btnAuth.isEnabled = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("APP_AUTH", "HP ini tidak memiliki fitur FingerPrint")
                msg = "HP ini tidak memiliki fitur FingerPrint"
                binding.btnAuth.isEnabled = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("APP_AUTH", "Fitur FingerPrint UNAVAILABLE")
                msg = "Fitur FingerPrint UNAVAILABLE"
                binding.btnAuth.isEnabled = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                //create FR credentials
                val errol = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                }
                binding.btnAuth.isEnabled = false

                startActivityForResult(errol, 100)
            }
        }
        binding.msgIndicator.text = msg
    }

    private fun createNewDataPenerima(){
        val intentCreate = intent.getIntExtra("intentCreatePenerima", 0)
        startActivity(Intent(this@BioAuthenticationActivity, EditPenerimaActivity::class.java)
            .putExtra("intentCreatePenerima", intentCreate)
        )
        finish()
    }
}