package com.example.tubes1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // var button
        val btnLogin_activity: Button = findViewById(R.id.selector_login)
        val btnRegister_activty: Button = findViewById(R.id.selector_Register)

        btnLogin_activity.isClickable = false
        btnLogin_activity.isEnabled = false


        btnLogin_activity.setOnClickListener {
            val move_to_login = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(move_to_login)
        }

        btnRegister_activty.setOnClickListener {
            val move_to_register = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(move_to_register)
        }
    }
}