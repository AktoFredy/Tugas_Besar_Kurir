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
import androidx.core.view.isEmpty
import androidx.lifecycle.lifecycleScope
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityLoginBinding
import com.example.tubes1.databinding.ActivityMainBinding
import com.example.tubes1.notification.NotificationReceiver
import com.example.tubes1.userSharedPreferences.PrefManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager


//    private lateinit var inputUsername_lgn: TextInputLayout
//    private lateinit var inputPassword_lgn: TextInputLayout
    //lateinit var temp_regBundle: Bundle

//    lateinit var penampung_username: String
//    lateinit var penampung_password: String

    //database
//    val dbU by lazy { UserDB(this) }

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    private var CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_login)
        setContentView(binding.root)

        prefManager = PrefManager(this)



//        inputUsername_lgn = findViewById(R.id.username)
//        inputPassword_lgn = findViewById(R.id.password)
//        val btnBack_ly2: ImageView = findViewById(R.id.btnBack_ly2)
//        val sign_up_click: Button = findViewById(R.id.register_click)
//        val btnLogin_click: Button = findViewById(R.id.btnLogin)

        getBundle()
//        penampung_username=""
//        penampung_password=""

        //==================================================================================================
        binding.registerClick.setOnClickListener {
            val move_to_register1 = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(move_to_register1)
        }

        binding.btnBackLy2.setOnClickListener {
            val move_to_main2 = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(move_to_main2)
        }
        //==================================================================================================

        //==================================================================
//        btnLogin_click.setOnClickListener (View.OnClickListener {
//            var cekLogin = false

//            if (inputUsername_lgn.getEditText()?.getText().toString() != ""){
//                CoroutineScope(Dispatchers.IO).launch {
//                    val user = dbU.userDao()
//                        .getUser(inputUsername_lgn.getEditText()?.getText().toString())[0]
//                    penampung_username = user.username
//                    penampung_password = user.password
//                    //Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
//                }
//            }
            //implementation lifecycle
//            lifecycleScope.launch {
//                getPembanding(inputUsername_lgn.getEditText()?.getText().toString())
//            }


//            val username: String = inputUsername_lgn.getEditText()?.getText().toString()
//            val password: String = inputPassword_lgn.getEditText()?.getText().toString()
            // Toast.makeText(applicationContext, inputUsername_lgn.editText?.text.toString(), Toast.LENGTH_LONG).show()
            //mencari data dari server
            //getPembanding(inputUsername_lgn.getEditText()?.getText().toString())

//            if(username.isEmpty()){
//                inputUsername_lgn.setError("Username Tidak Boleh Kosong")
//                cekLogin = false
//            }
//
//            if(password.isEmpty()){
//                inputPassword_lgn.setError("Password Tidak Boleh Kosong")
//                cekLogin = false
//            }

//            if(username == "admin" && password == "10778"){
//                cekLogin = true
//                var strUserName: String = penampung_username
//                var strPass: String = penampung_password
//                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
//                editor.putString(usernameK, strUserName)
//                editor.putString(passK, strPass)
//                editor.apply()
//            }

//            if(username == penampung_username && password == penampung_password && (penampung_username != "" || penampung_password != "")){
//                cekLogin = true
//                var strUserName: String = penampung_username
//                var strPass: String = penampung_password
//                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
//                editor.putString(usernameK, strUserName)
//                editor.putString(passK, strPass)
//                editor.apply()
//                createNotificationChannel()
//                sendNotification1()
//            }
//
//            if(username != penampung_username || password != penampung_password || penampung_username == "" || penampung_password == ""){
//                cekLogin = false
//                val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
//                builder.setTitle("Salah Password atau Username")
//                builder.setMessage("Isikan Password dan Username yang benar!!")
//                    .setPositiveButton("Yes"){ dialog, which ->
//                    }
//                    .show()
//            }
//            if(!cekLogin)return@OnClickListener
//            val move_to_home = Intent(this@LoginActivity, MainMenuActivity::class.java)
//            startActivity(move_to_home)
//        })

        binding.btnLogin.setOnClickListener{
            doLogin()
        }
    }

    fun getBundle(){

        //preference
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            binding.username.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            binding.password.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }

    fun doLogin(){
        val edtUser = binding.teksUser.text.toString()
        val edtPassword = binding.teksPassword.text.toString()

        server.instances.cekLoginUser(edtUser,edtPassword)
            .enqueue(object : Callback<ResponseLogin>{
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let { prefManager.setToken(it.token) }
                        response.body()?.let { prefManager.setEmail(it.email) }
                        response.body()?.let { prefManager.setUsername(it.username) }
                        response.body()?.let { prefManager.setNoTelepon(it.noTelepon) }

                        Toast.makeText(applicationContext, "${response.body()?.msg}", Toast.LENGTH_LONG).show()

                        val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                        editor.putString(usernameK, edtUser)
                        editor.putString(passK, edtPassword)
                        editor.apply()
                        createNotificationChannel()
                        sendNotification1()

                        startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java))
                        finish()
                    }else{
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        val messageError = JSONObject(jsonObj.getString("messages"))

                        if (messageError.has("error")){
                            binding.username.error = messageError.getString("error")
                            val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                            builder.setTitle("Username atau Password Salah!!")
                            builder.setMessage(messageError.getString("error"))
                                .setPositiveButton("OK"){ dialog, which ->
                                }
                                .show()
                            binding.teksPassword.setText("")
                            binding.teksUser.requestFocus()
                        }

                        if (messageError.has("username")){
                                binding.username.error = messageError.getString("username")
                        }else{
                            binding.username.error = null
                        }

                        if (messageError.has("userpassword")){
                            binding.password.error = messageError.getString("userpassword")
                        }else{
                            binding.password.error = null
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {

                }

            })
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
            .setContentTitle("User: " + binding.username.getEditText()?.getText().toString() + " Berhasil Login!!")
            .setContentText("Terima kasih sudah menggunakan MyCourier!!")
            .setLargeIcon(icon)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.terimakasihLogin))
                .setBigContentTitle("Terima kasih kakak")
                .setSummaryText("Username: " + binding.username.getEditText()?.getText().toString()))
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
}