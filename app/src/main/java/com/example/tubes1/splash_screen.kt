package com.example.tubes1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val prefs = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE)
        if (!prefs.getBoolean("bypass_boolean", false)) {
                val editor = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE).edit()
                editor.putBoolean("bypass_boolean", true)
                editor.apply()
                Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@splash_screen, MainActivity::class.java))
//                finish()
            },500)
        }else{
            startActivity(Intent(this@splash_screen, MainActivity::class.java))
            finish()
        }

//        val prefs = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE)
//        if (prefs.getBoolean("bypass_boolean", false)) {
//            val intent = Intent(this@splash_screen, MainActivity::class.java)
//            startActivity(intent)
//            val editor = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE).edit()
//            editor.putBoolean("bypass_boolean", true)
//            editor.apply()
//        }
    }

}