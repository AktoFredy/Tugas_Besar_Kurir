package com.example.tubes1

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityBufferSelectImageBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class BufferSelectImage : AppCompatActivity(),UploadRequestBody.UploadCallback {
    private lateinit var binding : ActivityBufferSelectImageBinding
    private var bundle: Bundle? = null
    private var imageURI: Uri? = null
    private lateinit var currentPhotoPath: String
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBufferSelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressUpImage.progress = 0

        bundle = intent.extras
        val idU = bundle?.getInt("idU")

        binding.btnFromGalery.setOnClickListener {
            getImageFromGalery()
        }

        binding.btnSaveImg.setOnClickListener {
            idU?.let { it1 -> uploadImage(it1) }
        }

        binding.btnFromCamera.setOnClickListener {
            takePictureFromCamera()
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    @RequiresApi(Build.VERSION_CODES.N)
    private fun takePictureFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                photoFile = createImageFile()

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    private fun uploadImage(idU: Int) {
        if (imageURI == null){
            Toast.makeText(applicationContext, "Select Picture First!!", Toast.LENGTH_LONG).show()
            return
        }

        val parcelFileDescriptor = contentResolver.openFileDescriptor(imageURI!!,"r", null)?:return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir,contentResolver.getFileName(imageURI!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val userfoto = UploadRequestBody(file, "image", this)

        server.instances.uploadUserImage(
            idU,
            MultipartBody.Part.createFormData("userfoto", file.name, userfoto),
            RequestBody.create(MediaType.parse("multipart/form-data"), "PUT")
        ).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Log.e(TAG, "onResponse: ${response.errorBody()!!.charStream().readText()}", )
                }
                binding.progressUpImage.progress = 100
            }

            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {

            }

        })
    }

    private fun getImageFromGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            imageURI = data?.data
            binding.imgFoto.setImageURI(imageURI)
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val uri = FileProvider.getUriForFile(applicationContext, "com.example.android.fileprovider", photoFile)
            imageURI = uri
            binding.imgFoto.setImageURI(imageURI)
        }
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressUpImage.progress = percentage
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}
