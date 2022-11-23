package com.example.tubes1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentHelpBinding
import com.example.tubes1.databinding.FragmentHomeBinding

class HelpFragment : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    private lateinit var view2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_help, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)
        view2 = binding.getRoot()
        binding.helep = "HELP"
        return view2
    }

    companion object {
    }
}