package com.example.tubes1.Kiriman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.tubes1.MainMenuActivity
import com.example.tubes1.R
import com.example.tubes1.databinding.ActivityKirimanCrudactivityBinding

class KirimanCRUDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKirimanCrudactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityKirimanCrudactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setItemListKiriman()

        binding.txtCariKiriman.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                setItemListKiriman()
                return@OnKeyListener true
            }
            false
        })

        binding.btnBackMainmenu.setOnClickListener {
            startActivity(Intent(this@KirimanCRUDActivity, MainMenuActivity::class.java))
        }

        binding.btnCreateKiriman.setOnClickListener {
            createNewBarang()
        }
    }

    private fun createNewBarang(){
        startActivity(Intent(this@KirimanCRUDActivity, EditKirimanActivity::class.java)
            .putExtra("intentCreate", 1)
        )
    }

    private fun setItemListKiriman(){
        val fragContent = KirimanFragment()
        val fragmentManager = supportFragmentManager
        val trans = fragmentManager.beginTransaction()
        val teksCari = binding.txtCariKiriman.text
        val bundle = Bundle()
        bundle.putString("cariKiriman", teksCari.toString())
        fragContent.arguments = bundle
        trans.replace(R.id.fragDataKiriman, fragContent).commit()
    }
}