package com.example.tubes1.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tubes1.*
import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.dataCrud.Pengiriman
import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.FragmentDeliveryBinding
import com.example.tubes1.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    //database
    val dbU by lazy { UserDB(requireActivity()) }

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var tvUsername: String? = null
    var tvPass: String? = null
    var penampungID: Int = 0

    //Shared Preference for login and register
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesProfile: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       //return inflater.inflate(R.layout.fragment_profile, container, false)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        getspData()
        getProfileData(tvUsername.toString())

        binding.rvBtnlogout2.setOnClickListener{
            logout()
        }

        binding.btnEdit2.setOnClickListener{
            intentEdit(penampungID, 2)
        }
    }

    fun getspData(){
        sharedPreferencesProfile = this.getActivity()?.getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesProfile!!.contains(usernameK)){
            tvUsername = sharedPreferencesProfile!!.getString(usernameK, "")
        }
        if (sharedPreferencesProfile!!.contains(passK)){
            tvPass = sharedPreferencesProfile!!.getString(passK, "")
        }
    }

    private fun getProfileData(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUser(str)[0]
            binding.profileUsername.setText(user.username)
            binding.profileEmail.setText(user.email)
            binding.profileNohp.setText(user.noTelepon)
            penampungID = user.idU
        }
    }

    private fun logout(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes"){ dialog, which ->
                requireActivity().finishAndRemoveTask()
            }
            .show()
    }

    fun intentEdit(id_input: Int, intentType: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
                .putExtra("intent_id", id_input)
                .putExtra("intent_type", intentType)
        )
    }
}