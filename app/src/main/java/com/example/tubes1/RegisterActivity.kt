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
import com.example.tubes1.databinding.ActivityRegisterBinding
import com.example.tubes1.notification.NotificationReceiver
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var cal = Calendar.getInstance()

//    var binding: ActivityRegisterBinding? = null
    val dbU by lazy { UserDB(this) }
    private var userId: Int = 0
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
//        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        //binding view==============
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(binding?.root)
        //==========================

        //Create notification Channel
        createNotificationChannelRegister()

        val move_to_login = Intent(this, LoginActivity::class.java)
//        val regBundle = Bundle()
//        regBundle.putString("username", "")
//        regBundle.putString("password", "")
//        regBundle.putString("email", "")
//        regBundle.putString("tglLahir", "")
//        regBundle.putString("tlp", "")
//        move_to_login.putExtra("register", regBundle)

        //preference
            sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
//        if (sharedPreferencesRegister!!.contains(usernameK)){
//            editTextName?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
//        }
//        if (sharedPreferencesRegister!!.contains(passK)){
//            editTextEmail?.setText(sharedPreferencesRegister!!.getString(passK, ""))
//        }

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
        binding?.tglEdt?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.loginClick?.setOnClickListener {
            startActivity(move_to_login)
        }

        binding?.btnBackLy1?.setOnClickListener{
            val move_to_main1 = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(move_to_main1)
        }

        binding?.btnRegis?.setOnClickListener (View.OnClickListener{
            var checkRegister = false

            val temp_username: String = binding?.inputUsername?.getEditText()?.getText().toString()
            val temp_password: String = binding?.inputPassword1?.getEditText()?.getText().toString()
            val temp_confirmPass: String = binding?.inputConfirmPassword?.getEditText()?.getText().toString()
            val temp_email: String = binding?.inputEmail?.getEditText()?.getText().toString()
            val temp_tglLahir: String = binding?.inputTanggalLahir?.getEditText()?.getText().toString()
            val temp_noTlp: String = binding?.inputTlp?.getEditText()?.getText().toString()

            if(temp_username.isEmpty()){
                binding?.inputUsername?.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_password.isEmpty()){
                binding?.inputPassword1?.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_confirmPass.isEmpty()){
                binding?.inputConfirmPassword?.setError("Password Confirm Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_password != temp_confirmPass){
                binding?.inputConfirmPassword?.setError("Password Confirm Harus Sama")
                checkRegister = false
            }

            if(temp_email.isEmpty()){
                binding?.inputEmail?.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_tglLahir.isEmpty()){
                binding?.inputTanggalLahir?.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_noTlp.isEmpty()){
                binding?.inputTlp?.setError("No Tlp Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_noTlp.length < 12){
                binding?.inputTlp?.setError("Panjang No Tlp harus > 12")
                checkRegister = false
            }

            if(!temp_username.isEmpty() && !temp_password.isEmpty() && !temp_confirmPass.isEmpty() && !temp_email.isEmpty() && !temp_tglLahir.isEmpty() && !temp_noTlp.isEmpty() && temp_noTlp.length == 12 && temp_password.equals(temp_confirmPass)){
                checkRegister = true

                CoroutineScope(Dispatchers.IO).launch {
                    dbU.userDao().addUser(
                        User(0, temp_email, temp_username, temp_password, temp_tglLahir, temp_noTlp)
                    )
                    finish()
                }

                //save to shareP
                var strUserName: String = binding.inputUsername.editText?.text.toString().trim()
                var strPass: String = binding.inputPassword1.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()

                //pop notification
                sendNotificationSucessRegister()
            }

            if(!checkRegister)return@OnClickListener
            startActivity(move_to_login)
        })
    }

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
        val formatku = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(formatku, Locale.US)
        temp = sdf.format(cal.getTime())
        binding?.inputTanggalLahir?.getEditText()?.setText(temp)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }
}