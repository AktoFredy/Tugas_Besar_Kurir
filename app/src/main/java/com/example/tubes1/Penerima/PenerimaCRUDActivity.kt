package com.example.tubes1.Penerima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.example.tubes1.MainMenuActivity
import com.example.tubes1.R
import com.example.tubes1.databinding.ActivityPenerimaCrudactivityBinding

class PenerimaCRUDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenerimaCrudactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityPenerimaCrudactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setItemListPenerima()
        binding.txtCariPenerima.setOnKeyListener(View.OnKeyListener{_, keyCode, event->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                setItemListPenerima()
                return@OnKeyListener true
            }
            false
        })

        binding.btnBackMainmenu.setOnClickListener {
            startActivity(Intent(this@PenerimaCRUDActivity, MainMenuActivity::class.java))
        }

        binding.btnCreatePenerima.setOnClickListener {
            createNewDataPenerima()
        }
    }

    private fun createNewDataPenerima(){
        startActivity(Intent(this@PenerimaCRUDActivity, EditPenerimaActivity::class.java)
            .putExtra("intentCreatePenerima", 1)
        )
    }

    private fun setItemListPenerima(){
        val fragContent = PenerimaFragment()
        val fragmentManager = supportFragmentManager
        val trans = fragmentManager!!.beginTransaction()
        val teksCari = binding.txtCariPenerima.text
        val bundle = Bundle()
        bundle.putString("cariPenerima", teksCari.toString())
        fragContent.arguments = bundle
        trans.replace(R.id.fragDataPenerima, fragContent).commit()
    }
}