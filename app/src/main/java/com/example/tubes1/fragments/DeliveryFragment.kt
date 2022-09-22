package com.example.tubes1.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.EditActivity
import com.example.tubes1.MainActivity
import com.example.tubes1.R
import com.example.tubes1.RVDummyAdapter
import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.dataCrud.Pengiriman
import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.FragmentDeliveryBinding
import com.example.tubes1.entity.dummy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryFragment : Fragment() {
    val db by lazy { PengirimanDB(requireActivity()) }
    lateinit var pengirimanAdapter : RVDummyAdapter
    private var _binding: FragmentDeliveryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_delivery, container, false)
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
//        val layoutManager = LinearLayoutManager(context)
//        val adapter : RVDummyAdapter = RVDummyAdapter(dummy.listOfDummy)
//
//        val rvDummy : RecyclerView = view.findViewById(R.id.rv_deliv)
//
//        rvDummy.layoutManager = layoutManager
//
//        rvDummy.setHasFixedSize(true)
//
//        rvDummy.adapter = adapter
        setupListener()
        setupRecyclerView()
    }
    private fun setupRecyclerView(){
        pengirimanAdapter = RVDummyAdapter(arrayListOf(), object : RVDummyAdapter.OnAdapterListener{

            override fun onClick(pengiriman: Pengiriman) {
                intentEdit(pengiriman.idP, CrudCons.TYPE_READ)
            }

            override fun onUpdate(pengiriman: Pengiriman) {
                intentEdit(pengiriman.idP, CrudCons.TYPE_UPDATE)
            }

            override fun onDelete(pengiriman: Pengiriman) {
                deleteDialog(pengiriman)
            }
        })
        binding.rvDeliv.apply {
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            adapter = pengirimanAdapter
        }
    }

    private fun deleteDialog(pengiriman: Pengiriman){
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You sure to delete this data From ${pengiriman.kotaTujuan}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.PengirimanDao().deletePengiriman(pengiriman)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart(){
        super.onStart()
        loadData()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val pengirimans = db.PengirimanDao().getPengiriman1()
            Log.d("DeliveryFragment", "dbResponse: $pengirimans")
            withContext(Dispatchers.Main){
                pengirimanAdapter.setData(pengirimans)
            }
        }
    }

    fun setupListener(){
        binding.btnCreate.setOnClickListener{
            intentEdit(0, CrudCons.TYPE_CREATE)
        }
    }

    fun intentEdit(pengirimanId: Int, intentType: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditActivity::class.java)
                .putExtra("intent_id", pengirimanId)
                .putExtra("intent_type", intentType)
        )
    }
}