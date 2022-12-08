package com.example.tubes1.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentHelpBinding
import com.example.tubes1.fragments.faq.FaqFragment

class HelpFragment : Fragment() {
    private lateinit var binding: FragmentHelpBinding
    private lateinit var view2: View
    private var installed: Boolean = false
    private val msgText: String = "Hi, Customer Service myCourier, Saya ingin mengajukan pertanyaan mengenai paket saya pada aplikasi MyCourier, \n\nNo Resi: \nNama: \nNomor \nTlp: \nTgl: \nDetails: "
    private val noTlp: String = "85669865451"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wabtn.setOnClickListener{
            appIsThere("com.whatsapp")
            if (installed){
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+62" + noTlp + "&text=" + msgText))
                startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), "whatsapp not installed on your device", Toast.LENGTH_SHORT).show()
            }
        }

        binding.calus.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:" + "+62" + noTlp))
            startActivity(intent)
        }

        binding.faqbtn.setOnClickListener {
            val fragment = FaqFragment()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    companion object {
    }

    private fun appIsThere(url: String){
        val packageManager: PackageManager = requireActivity()!!.getPackageManager()
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            installed = true
        }catch (e: PackageManager.NameNotFoundException){
            installed = false
        }
    }
}