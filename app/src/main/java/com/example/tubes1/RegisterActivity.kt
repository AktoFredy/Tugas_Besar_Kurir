package com.example.tubes1

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityRegisterBinding
import com.example.tubes1.notification.NotificationReceiver
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var cal = Calendar.getInstance()

    private var userId: Int = 0

    //init binding
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivityRegisterBinding

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    //var notif
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        //binding view==============
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //prefmanager
        prefManager = PrefManager(this)

        //Create notification Channel
        createNotificationChannelRegister()

        //preference
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        // untuk datePicker
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        //menggunakan inputLayout yang diset click
        binding?.edttgllahir?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.loginClick?.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding?.btnBackLy1?.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        }

        binding?.btnRegis?.setOnClickListener{
            val temp_username: String = binding?.inputUsername?.getEditText()?.text.toString()
            val temp_password: String = binding?.inputPassword1?.getEditText()?.text.toString()
            val temp_confirmPass: String = binding?.inputConfirmPassword?.getEditText()?.text.toString()
            val temp_email: String = binding?.inputEmail?.getEditText()?.text.toString()
            val temp_tglLahir: String = binding?.inputTanggalLahir?.getEditText()?.text.toString()
            val temp_noTlp: String = binding?.inputTlp?.getEditText()?.text.toString()

            Toast.makeText(applicationContext, temp_username, Toast.LENGTH_SHORT).show()

            if (temp_email.isEmpty() || temp_email == ""){
                binding.inputEmail.setError("Email tidak boleh kosong")
            }
            if(temp_username.isEmpty() || temp_username == ""){
                binding?.inputUsername?.setError("Username Tidak Boleh Kosong")
            }
            if(temp_password.isEmpty() || temp_password == ""){
                binding?.inputPassword1?.setError("Password Tidak Boleh Kosong")
            }
            if(temp_confirmPass.isEmpty() || temp_confirmPass == ""){
                binding?.inputConfirmPassword?.setError("Password Confirm Tidak Boleh Kosong")
            }
            if(temp_password != temp_confirmPass){
                binding?.inputConfirmPassword?.setError("Password dan Confirm Password Harus Sama")
            }
            if(temp_email.isEmpty() || temp_email == ""){
                binding?.inputEmail?.setError("Email Tidak Boleh Kosong")
            }
            if(temp_tglLahir.isEmpty() || temp_tglLahir == ""){
                binding?.inputTanggalLahir?.setError("Tanggal Lahir Tidak Boleh Kosong")
            }
            if(temp_noTlp.isEmpty() || temp_noTlp == ""){
                binding?.inputTlp?.setError("No Tlp Tidak Boleh Kosong")
            }
            if(temp_noTlp.length < 12){
                binding?.inputTlp?.setError("Panjang No Tlp harus >= 12")
            }
            if (!temp_email.isEmpty() && temp_email != "" && !temp_username.isEmpty() && temp_username != "" && !temp_password.isEmpty() && temp_password != "" && !temp_confirmPass.isEmpty() && temp_confirmPass != "" && temp_password == temp_confirmPass && !temp_email.isEmpty() && temp_email != "" && !temp_tglLahir.isEmpty() && temp_tglLahir != "" && !temp_noTlp.isEmpty() && temp_noTlp != "" && temp_noTlp.length >= 12){
                val token_auth = "Bearer ${prefManager.getToken()}"

                server.instances.createDataUser(token_auth, temp_email, temp_username, temp_password, temp_tglLahir, temp_noTlp).enqueue(object : Callback<ResponseCreate>{
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_SHORT).show()
                        val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                        editor.putString(usernameK, temp_username)
                        editor.putString(passK, temp_password)
                        editor.apply()

                        //pop notification
                        sendNotificationSucessRegister()

                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {}
                })
            }
        }
    }

    //==============================================================================================
    private fun createNotificationChannelRegister(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val title = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val channel2 = NotificationChannel(CHANNEL_ID_2, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotificationSucessRegister(){
        val intent: Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Terima kasih sudah Register")
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_done_all)
            .setContentTitle("User: " + binding?.inputUsername?.editText?.text.toString() + " Berhasil Registrasi!!")
            .setContentText("Terima kasih sudah menggunakan MyCourier!!")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.gambar_logo)))
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

    private fun updateEditText(){
        var temp : String
        val formatku = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(formatku, Locale.US)
        temp = sdf.format(cal.getTime())
        binding?.inputTanggalLahir?.getEditText()?.setText(temp)
    }
}