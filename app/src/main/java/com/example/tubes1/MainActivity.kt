package com.example.tubes1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.ActivityMainBinding
import com.example.tubes1.databinding.ActivityRegisterBinding

class MainActivity : AppCompatActivity() {

    var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // var button
//        val btnLogin_activity: Button = findViewById(R.id.selector_login)
//        val btnRegister_activty: Button = findViewById(R.id.selector_Register)
//
//        btnLogin_activity.isClickable = false
//        btnLogin_activity.isEnabled = false

        binding?.selectorLogin?.isClickable = false
        binding?.selectorLogin?.isEnabled = false

        binding?.selectorLogin?.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        binding?.selectorRegister?.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }
    }
}