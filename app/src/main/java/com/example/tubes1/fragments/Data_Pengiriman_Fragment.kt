package com.example.tubes1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1.*
import com.example.tubes1.client.server
import com.example.tubes1.databinding.FragmentDataPengirimanBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE")
class Data_Pengiriman_Fragment : Fragment() {
    private var _binding: FragmentDataPengirimanBinding? = null

    private val binding get() = _binding!!

    private val listPengiriman = ArrayList<PengirimanData>()
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDataPengirimanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onStart(){
        super.onStart()
        loadData()
    }

    fun loadData(){
        binding.rvDeliv.setHasFixedSize(true)
        binding.rvDeliv.layoutManager = LinearLayoutManager(context)

        val bundle = arguments
        val cari = bundle?.getString("cari")

        val token_auth = "Bearer ${prefManager.getToken()}"

        binding.progressBar.visibility

        server.instances.getData(token_auth, cari).enqueue(object : Callback<ResponseDataPengiriman> {
            override fun onResponse(
                call: Call<ResponseDataPengiriman>,
                response: Response<ResponseDataPengiriman>
            ){
                if (response.isSuccessful){
                    listPengiriman.clear()
                    response.body()?.let { listPengiriman.addAll(it.data) }

                    var adapter = RVDummyAdapter(listPengiriman, requireContext())
                    binding.rvDeliv.adapter = adapter
                    adapter.notifyDataSetChanged()

                    binding.progressBar.isVisible = false
                }else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    Toast.makeText(context, "${jsonObj.getString("messages")}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataPengiriman>, t: Throwable) {}
        })
    }

}