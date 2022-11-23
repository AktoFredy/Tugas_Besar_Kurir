package com.example.tubes1.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tubes1.*
import com.example.tubes1.databinding.FragmentDeliveryBinding

@Suppress("UNREACHABLE_CODE")
class DeliveryFragment : Fragment() {
    private var _binding: FragmentDeliveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var idPengiriman = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment()

        binding.txtCari.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            {
                loadFragment()
                return@OnKeyListener true
            }
            false
        })

        binding.btnCreate.setOnClickListener{
            startActivity(
                Intent(requireActivity(), EditActivity::class.java)
                    .putExtra("intent_id", idPengiriman)
            )
        }

        binding.btnScanQR.setOnClickListener {
            startActivity(Intent(requireActivity(), PengirimanDetailsQRScannerActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadFragment(){
        val mfragment = Data_Pengiriman_Fragment()
        val mFragmentManager = getFragmentManager()
        val mfTransaction = mFragmentManager!!.beginTransaction()
        val teksCari = binding.txtCari.text
        val mBundle = Bundle()
        mBundle.putString("cari", teksCari.toString())
        mfragment.arguments = mBundle
        mfTransaction.replace(R.id.fl_data, mfragment).commit()
    }
}