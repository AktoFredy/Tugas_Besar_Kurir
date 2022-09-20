package com.example.tubes1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.R
import com.example.tubes1.RVDummyAdapter
import com.example.tubes1.entity.dummy

class DeliveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDummyAdapter = RVDummyAdapter(dummy.listOfDummy)

        val rvDummy : RecyclerView = view.findViewById(R.id.rv_deliv)

        rvDummy.layoutManager = layoutManager

        rvDummy.setHasFixedSize(true)

        rvDummy.adapter = adapter
    }
}