package com.example.tubes1.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.tubes1.*
import com.example.tubes1.dataCrud.CrudCons
import com.example.tubes1.dataCrud.Pengiriman
import com.example.tubes1.dataCrud.PengirimanDB
import com.example.tubes1.databinding.FragmentDeliveryBinding
import com.example.tubes1.databinding.FragmentProfileBinding
import com.example.tubes1.notification.NotificationReceiver
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.BroadcastReceiver
import android.content.Context.NOTIFICATION_SERVICE
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemServiceName
import com.example.tubes1.databinding.ActivityMainBinding


class ProfileFragment : Fragment() {


    //database
    val dbU by lazy { UserDB(requireActivity()) }
    private var CHANNEL_ID_1 = "channel_notification_01"
    private var notificationId1 = 101

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

        binding.rvBtnDownload.setOnClickListener{
            sendNotifiaction1()
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


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1,name,NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotifiaction1(){
        val intent: Intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val broadcastIntent: Intent = Intent(this,NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", R.id.username.toString())
        val actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_download_24)
            .setContentTitle("title")
            .setContentText("Notification Progress bar")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher,"Toast",actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }

}