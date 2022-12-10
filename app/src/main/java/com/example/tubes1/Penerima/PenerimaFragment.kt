package com.example.tubes1.Penerima

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1.client.server
import com.example.tubes1.databinding.FragmentPenerimaBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenerimaFragment : Fragment() {
    private var _binding: FragmentPenerimaBinding? = null

    private val binding get() = _binding!!

    private val listPenerima = ArrayList<PenerimaData>()
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPenerimaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        loadPenerima()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        loadPenerima()
    }

    private fun loadPenerima(){
        binding.rvPenerima.setHasFixedSize(true)
        binding.rvPenerima.layoutManager = LinearLayoutManager(context)

        val bundle = arguments
        val cari = bundle?.getString("cariPenerima")

        val token = "Bearer ${prefManager.getToken()}"
        binding.progressBarPenerima.visibility

        server.instances.getDataPenerima(token, cari).enqueue(object : Callback<ResponseDataPenerima>{
            override fun onResponse(
                call: Call<ResponseDataPenerima>,
                response: Response<ResponseDataPenerima>
            ) {
                if (response.isSuccessful){
                    listPenerima.clear()
                    response.body()?.let { listPenerima.addAll(it.data) }

                    val adapter = PenerimaRVAdapter(listPenerima, requireContext())
                    binding.rvPenerima.adapter = adapter
                    binding.progressBarPenerima.isVisible = false
                }else{
                    val jsonobj = JSONObject(response.errorBody()!!.charStream().readText())
                    Toast.makeText(context, "${jsonobj.getString("messages")}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataPenerima>, t: Throwable) {
            }

        })
    }

}