package com.example.tubes1.Kiriman

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
import com.example.tubes1.databinding.FragmentKirimanBinding
import com.example.tubes1.userSharedPreferences.PrefManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KirimanFragment : Fragment() {
    private var _binding: FragmentKirimanBinding? = null

    private val binding get() = _binding!!

    private val listKiriman = ArrayList<KirimanData>()
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKirimanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        loadKiriman()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        loadKiriman()
    }

    private fun loadKiriman(){
        binding.rvKiriman.setHasFixedSize(true)
        binding.rvKiriman.layoutManager = LinearLayoutManager(context)

        val bundle = arguments
        val cari = bundle?.getString("cariKiriman")

        val token = "Bearer ${prefManager.getToken()}"

        binding.progressBarKiriman.visibility

        server.instances.getDataKiriman(token, cari).enqueue(object : Callback<ResponseDataKiriman> {
            override fun onResponse(
                call: Call<ResponseDataKiriman>,
                response: Response<ResponseDataKiriman>
            ) {
                if (response.isSuccessful){
                    listKiriman.clear()
                    response.body()?.let { listKiriman.addAll(it.data) }

                    val adapter = KirimanRvAdapter(listKiriman, requireContext())
                    binding.rvKiriman.adapter = adapter

                    binding.progressBarKiriman.isVisible = false
                }else{
                    val jsonobj = JSONObject(response.errorBody()!!.charStream().readText())
                    Toast.makeText(context, "${jsonobj.getString("messages")}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataKiriman>, t: Throwable) {

            }

        })
    }
}