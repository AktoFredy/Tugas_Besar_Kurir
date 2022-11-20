package com.example.tubes1.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.*
import com.example.tubes1.client.server
//import com.example.tubes1.dataCrud.CrudCons
//import com.example.tubes1.dataCrud.Pengiriman
//import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.FragmentDeliveryBinding
import com.example.tubes1.entity.dummy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class DeliveryFragment : Fragment() {
    private var _binding: FragmentDeliveryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var idPengiriman = -1

    private val listPengiriman = ArrayList<PengirimanData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        //loadFragment()
        return binding.root
//        loadData()


//        binding.txtCari.setOnKeyListener(View.OnKeyListener{ _, keyCode, event->
//            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
//            {
////                loadData()
//
//                return@OnKeyListener true
//            }
//            false
//        })
//
//        binding.btnCreate.setOnClickListener{
//            loadFragment()
//            startActivity(Intent(requireActivity(), EditActivity::class.java))
//        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    override fun onStart(){
//        super.onStart()
////        loadData()
//        loadFragment()
//    }

//    fun loadData(){
//        binding.rvDeliv.setHasFixedSize(true)
//        binding.rvDeliv.layoutManager = LinearLayoutManager(context)
//
//        val cari = binding.txtCari.text.toString()
//
//        binding.progressBar.visibility
//
//        server.instances.getData(cari).enqueue(object : Callback<ResponseDataPengiriman> {
//            override fun onResponse(
//                call: Call<ResponseDataPengiriman>,
//                response: Response<ResponseDataPengiriman>
//            ){
//                if (response.isSuccessful){
//                    listPengiriman.clear()
//                    response.body()?.let { listPengiriman.addAll(it.data) }
//
//                    var adapter = RVDummyAdapter(listPengiriman, requireContext())
//                    binding.rvDeliv.adapter = adapter
//                    adapter.notifyDataSetChanged()
//
//                    binding.progressBar.isVisible = false
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseDataPengiriman>, t: Throwable) {
//
//            }
//
//
//        }
//        )
//    }

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