package com.example.tubes1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setTitle("Update Profile")
    }
}