package com.example.tubes1.fragments.faq

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1.MainMenuActivity
import com.example.tubes1.client.server
import com.example.tubes1.databinding.FragmentFaqBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    private val listFaqs = ArrayList<FaqData>()
    private var cari: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDataFaqs()
    }

    override fun onStart() {
        super.onStart()

        loadDataFaqs()
    }

    private fun loadDataFaqs(){
        binding.itemfaq.setHasFixedSize(true)
        binding.itemfaq.layoutManager = LinearLayoutManager(context)

        //menggunakan response data pengiriman karena data yang dilempar sama
        server.instances.getDataFAQ(cari).enqueue(object : Callback<ResponseDataFaqs>{
            override fun onResponse(
                call: Call<ResponseDataFaqs>,
                response: Response<ResponseDataFaqs>
            ) {
                if (response.isSuccessful){
                    listFaqs.clear()
                    response.body()?.let { listFaqs.addAll(it.data) }

                    var adapter = FaqRVAdapter(listFaqs, requireContext())
                    binding.itemfaq.adapter = adapter
                    adapter.notifyDataSetChanged()

                }else{
                    val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
                    Toast.makeText(context, "${jsonObject.getString("message")}", Toast.LENGTH_LONG).show()
                    startActivity(Intent(context, MainMenuActivity::class.java))
                }
            }

            override fun onFailure(call: Call<ResponseDataFaqs>, t: Throwable) {
            }

        })

    }

}