package com.example.tubes1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.tubes1.databinding.ActivityMainBinding
import com.example.tubes1.notification.NotificationReceiver
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername_lgn: TextInputLayout
    private lateinit var inputPassword_lgn: TextInputLayout
    //lateinit var temp_regBundle: Bundle

    lateinit var penampung_username: String
    lateinit var penampung_password: String

    //database
    val dbU by lazy { UserDB(this) }

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    private var CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        inputUsername_lgn = findViewById(R.id.username)
        inputPassword_lgn = findViewById(R.id.password)
        val btnBack_ly2: ImageView = findViewById(R.id.btnBack_ly2)
        val sign_up_click: Button = findViewById(R.id.register_click)
        val btnLogin_click: Button = findViewById(R.id.btnLogin)

        getBundle()
        penampung_username=""
        penampung_password=""

        sign_up_click.setOnClickListener {
            val move_to_register1 = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(move_to_register1)
        }

        btnBack_ly2.setOnClickListener {
            val move_to_main2 = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(move_to_main2)
        }
        btnLogin_click.setOnClickListener (View.OnClickListener {
            var cekLogin = false

//            CoroutineScope(Dispatchers.IO).launch {
//                val user = dbU.userDao().getUser(inputUsername_lgn.getEditText()?.getText().toString())[0]
//                penampung_username = user.username
//                penampung_password = user.password
//                //Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
//            }
            //implementation lifecycle
            lifecycleScope.launch {
                getPembanding(inputUsername_lgn.getEditText()?.getText().toString())
            }


            val username: String = inputUsername_lgn.getEditText()?.getText().toString()
            val password: String = inputPassword_lgn.getEditText()?.getText().toString()
            // Toast.makeText(applicationContext, inputUsername_lgn.editText?.text.toString(), Toast.LENGTH_LONG).show()
            //mencari data dari server
            //getPembanding(inputUsername_lgn.getEditText()?.getText().toString())

            if(username.isEmpty()){
                inputUsername_lgn.setError("Username Tidak Boleh Kosong")
                cekLogin = false
            }

            if(password.isEmpty()){
                inputPassword_lgn.setError("Password Tidak Boleh Kosong")
                cekLogin = false
            }

            if(username == "admin" && password == "10778"){
                cekLogin = true
                var strUserName: String = penampung_username
                var strPass: String = penampung_password
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()
            }
            if(username == penampung_username && password == penampung_password){
                cekLogin = true
                var strUserName: String = penampung_username
                var strPass: String = penampung_password
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()
                createNotificationChannel()
                sendNotification1()
            }

            if(username != penampung_username || password != penampung_password){
                cekLogin = false
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("Salah Password atau Username")
                builder.setMessage("Isikan Password dan Usrname yang benar!!")
                    .setPositiveButton("Yes"){ dialog, which ->
                    }
                    .show()
            }
            if(!cekLogin)return@OnClickListener
            val move_to_home = Intent(this@LoginActivity, MainMenuActivity::class.java)
            startActivity(move_to_home)
        })
    }

    fun getBundle(){

        //preference
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            inputUsername_lgn.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            inputPassword_lgn.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification1(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", R.id.username.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val icon: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arigatou)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_login)
            .setContentTitle("User: " + inputUsername_lgn.getEditText()?.getText().toString() + " Berhasil Login!!")
            .setContentText("Terima kasih sudah menggunakan MyCourier!!")
            .setLargeIcon(icon)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.terimakasihLogin))
                .setBigContentTitle("Terima kasih kakak")
                .setSummaryText("Username: " + inputUsername_lgn.getEditText()?.getText().toString()))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

    fun getPembanding(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUser(str)[0]
            penampung_username = user.username
            penampung_password = user.password
            //Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
        }
    }
}