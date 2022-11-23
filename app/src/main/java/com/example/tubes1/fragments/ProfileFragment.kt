package com.example.tubes1.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
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
import com.example.tubes1.*
import com.example.tubes1.databinding.FragmentProfileBinding
import android.os.SystemClock
import com.bumptech.glide.Glide
import com.example.tubes1.client.server
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    //channel init notification
    private var CHANNEL_ID_1 = "channel_notification_01"
    private var CHANNEL_ID_2 = "channel_notification_02"
    private var notificationId1 = 101
    private var notificationId2 = 102

    private val listUsers = ArrayList<UsersData>()
    private lateinit var prefManager: PrefManager
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

        createNotificationChannel()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        getspData()
        getProfileData(tvUsername.toString())

        binding.profileImage.setOnClickListener{
//            openCamera()

            //edited
            startActivity(Intent(requireActivity(), BufferSelectImage::class.java).apply {
                putExtra("idU", penampungID)
            })
        }

        binding.rvBtnlogout2.setOnClickListener{
            logout()
        }

        binding.btnEdit2.setOnClickListener{
            intentEdit(penampungID)
        }

        //pengunaan notification
        binding.rvBtnDownload.setOnClickListener{
            sendNotifiaction1()
        }

        binding.rvBtnPlayer.setOnClickListener{
            sendNotifiaction2()
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

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataUser(token_auth, tvUsername).enqueue(object: Callback<ResponseDataUsers>{
            override fun onResponse(
                call: Call<ResponseDataUsers>,
                response: Response<ResponseDataUsers>
            ) {
                if (response.isSuccessful){
                    listUsers.clear()
                    response.body().let { it?.let { it1 -> listUsers.addAll(it1.data) } }

                    if (listUsers.size == 1){
                        binding.profileUsername.setText(listUsers[0].username)
                        binding.profileEmail.setText(listUsers[0].email)
                        binding.profileNohp.setText(listUsers[0].noTelepon)
                        penampungID = listUsers[0].idU

                        Glide.with(requireContext())
                            .load("${server.BASE_URL+"uploads/" + listUsers[0].userfoto}")
                            .into(binding.profileImage)
                    }else{
                        for (item in listUsers){
                            if (item.username == tvUsername){
                                binding.profileUsername.setText(item.username)
                                binding.profileEmail.setText(item.email)
                                binding.profileNohp.setText(item.noTelepon)
                                penampungID = item.idU

                                Glide.with(requireContext())
                                    .load("${server.BASE_URL+"uploads/" + item.userfoto}")
                                    .into(binding.profileImage)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataUsers>, t: Throwable) {

            }

        })
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

    //==============================================================================================
    fun intentEdit(id_input: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
        )
    }

    //==============================================================================================
    fun openCamera(){
        startActivity(
            Intent(requireActivity().applicationContext, Camera_Activity::class.java)
        )
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1,name,NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getActivity()?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotifiaction1(){

        val progressMax = 100
        var progress: Int = 0

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_download_24)
            .setContentTitle("Download")
            .setContentText("Download in Progress")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(progressMax, 0, false)


        with(NotificationManagerCompat.from(requireActivity())){
            notify(notificationId1, builder.build())
        }
        Thread(Runnable {
            run(){
                SystemClock.sleep(2000)
                while (progress <= progressMax){
                    progress += 10
                    builder.setProgress(progressMax, progress, false)
                    with(NotificationManagerCompat.from(requireActivity())){
                        notify(notificationId1, builder.build())
                        SystemClock.sleep(1000)
                    }
                }
                builder.setContentText("Download Finised")
                    .setProgress(0,0, false)
                    .setOngoing(false)
                with(NotificationManagerCompat.from(requireActivity())){
                    notify(notificationId1, builder.build())
                    SystemClock.sleep(1000)
                }
            }
        }).start()
    }

    private fun sendNotifiaction2(){
        val art = BitmapFactory.decodeResource(getResources(), R.drawable.whiteeye)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle("Music Player Example")
            .setContentText("Running Songs...")
            .setLargeIcon(art)
            .addAction(R.drawable.ic_surround_sound, "EnableSourround", null)
            .addAction(R.drawable.ic_previous, "Previous", null)
            .addAction(R.drawable.ic_play, "Play", null)
            .addAction(R.drawable.ic_stop, "Stop", null)
            .addAction(R.drawable.ic_next, "Next", null)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1, 2, 3))
            .setSubText("Pyaing Example")


        with(NotificationManagerCompat.from(requireActivity())){
            notify(notificationId1, builder.build())
        }
    }
}