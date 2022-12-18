package com.example.tubes1

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tubes1.Kiriman.KirimanData
import com.example.tubes1.Kiriman.ResponseDataKiriman
import com.example.tubes1.Penerima.PenerimaData
import com.example.tubes1.Penerima.ResponseDataPenerima
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityEditBinding
import com.example.tubes1.fragments.DeliveryFragment
import com.example.tubes1.userSharedPreferences.PrefManager
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_edit.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditActivity : AppCompatActivity() {

    private var Biaya: Double = 0.00
    private var idPengiriman: Int = 0
    private lateinit var binding: ActivityEditBinding
    private lateinit var prefManager: PrefManager

    //list data
    private val listPengiriman = ArrayList<PengirimanData>()
    private val listPenerima = ArrayList<PenerimaData>()
    private val listKiriman = ArrayList<KirimanData>()

    //array dropdown
    private val arrayNamaPenerima = ArrayList<String>()
    private val arrayNamaKiriman = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.title = "Pengirimman"

        binding = ActivityEditBinding.inflate(layoutInflater)
        prefManager = PrefManager(this)

        setContentView(binding.root)
        setupView()
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        getList()
        val kotaPenerima = resources.getStringArray(R.array.kotaPenerima)
        val arrayAdapterKota = ArrayAdapter(this, R.layout.dropdown_item, kotaPenerima)
        val arrayAdapterNamaPenerima = ArrayAdapter(this, R.layout.dropdown_item, arrayNamaPenerima)
        val arrayAdapterNamaKiriman = ArrayAdapter(this, R.layout.dropdown_item, arrayNamaKiriman)

        //set data to component
        binding.autoCompleteKotaPenerima.setAdapter(arrayAdapterKota)
        binding.autoCompleteKotaPengirim.setAdapter(arrayAdapterKota)
        binding.autoCompleteNamaPenerima.setAdapter(arrayAdapterNamaPenerima)
        binding.autoCompleteIsiKiriman.setAdapter(arrayAdapterNamaKiriman)
    }

    fun biayacost(){
        if(binding.inputKotaPenerima.editText?.text.toString() == "Jakarta"){
            Biaya = 50000.00
        }else if(binding.inputKotaPenerima.editText?.text.toString() == "Yogyakarta"){
            Biaya = 20000.00
        }else{
            Biaya = 25000.00
        }
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentID = intent.getIntExtra("intent_id", -1)
        idPengiriman = intentID
        if (intentID < 0){
            binding.btnUpdate.visibility = View.GONE
        }else{
            binding.btnSave.visibility = View.GONE
            getPengiriman()
        }
    }

    fun setupListener(){
        binding.btnSave.setOnClickListener{
            with(binding){
                val namaPengirim = inputPengirim.editText?.text.toString()
                val namaPenerima = inputPenerima.editText?.text.toString()
                val desBarang = inputIsiKiriman.editText?.text.toString()
                val kotaAsal = inputKotaPengirim.editText?.text.toString()
                val kotaTujuan = inputKotaPenerima.editText?.text.toString()
                val alamatLengkap = inputAlamatLengkap.editText?.text.toString()
                biayacost()

                val token_auth = "Bearer ${prefManager.getToken()}"

                server.instances.createData(token_auth,namaPengirim,namaPenerima,desBarang,kotaAsal,kotaTujuan,alamatLengkap,Biaya).enqueue(object :
                    Callback<ResponseCreate> {
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if(response.isSuccessful){
                            //creating pdf file
                            //createPdf(namaPengirim, namaPenerima, desBarang, kotaAsal, kotaTujuan, alamatLengkap, Biaya)

                            // finish saving to database
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@EditActivity, MainMenuActivity::class.java))
                            finish()
                        }else{
                            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                            val errMsg = JSONObject(jsonObj.getString("message"))

                            if (errMsg != null){
                                if (errMsg.has("namaPengirim")){
                                    binding.inputPengirim.error = errMsg.getString("namaPengirim")
                                }else{
                                    binding.inputPengirim.error = null
                                }

                                if (errMsg.has("namaPenerima")){
                                    binding.inputPenerima.error = errMsg.getString("namaPenerima")
                                }else{
                                    binding.inputPenerima.error = null
                                }

                                if (errMsg.has("desBarang")){
                                    binding.inputIsiKiriman.error = errMsg.getString("desBarang")
                                }else{
                                    binding.inputIsiKiriman.error = null
                                }

                                if (errMsg.has("kotaAsal")){
                                    binding.inputKotaPengirim.error = errMsg.getString("kotaAsal")
                                }else{
                                    binding.inputKotaPengirim.error = null
                                }

                                if (errMsg.has("kotaTujuan")){
                                    binding.inputKotaPenerima.error = errMsg.getString("kotaTujuan")
                                }else{
                                    binding.inputKotaPenerima.error = null
                                }

                                if (errMsg.has("alamatLengkap")){
                                    binding.inputAlamatLengkap.error = errMsg.getString("alamatLengkap")
                                }else{
                                    binding.inputAlamatLengkap.error = null
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                    }

                })
            }


        }

        binding.btnUpdate.setOnClickListener{
            with(binding){
                val namaPengirim = inputPengirim.editText?.text.toString()
                val namaPenerima = inputPenerima.editText?.text.toString()
                val desBarang = inputIsiKiriman.editText?.text.toString()
                val kotaAsal = inputKotaPengirim.editText?.text.toString()
                val kotaTujuan = inputKotaPenerima.editText?.text.toString()
                val alamatLengkap = inputAlamatLengkap.editText?.text.toString()
                biayacost()

                val token_auth = "Bearer ${prefManager.getToken()}"

                server.instances.updateData(token_auth,idPengiriman,namaPengirim,namaPenerima,desBarang,kotaAsal,kotaTujuan,alamatLengkap,Biaya).enqueue(object :
                Callback<ResponseCreate>{
                    override fun onResponse(
                        call: Call<ResponseCreate>,
                        response: Response<ResponseCreate>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {}
                })
            }

        }
    }


    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(namaPengirim: String, namaPenerima: String, desBarang: String, kotaAsal: String, kotaTujuan: String, alamatLengkap: String, biaya: Double) {
        val counterPDF = prefManager.getPDFcounter()
        val nameuserLOGIN = prefManager.getUsername()
        // ini berguna untuk akses writing ke storage hp kalian dalam model download
        //harus diketik jangan COPAS
        var id = counterPDF + 1
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Pdforder" + id + ".pdf")
        FileOutputStream(file)
        prefManager.setIdPdfUP(id)
        //inisialisasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.logo_noback)

        //penambahan gambar pada Gambar atas
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        image.scaleToFit(300F, 300F).setRelativePosition(137F, 5F, 130F, 5F)
        val namaPengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        created by User: ${nameuserLOGIN}
                        Universitas Atma Jaya Yogyakarta 2022/2023
                        Fakultas Teknologi Industri
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan tabel
        val width = floatArrayOf(150f, 150f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Pengirim")))
        table.addCell(Cell().add(Paragraph(namaPengirim)))
        table.addCell(Cell().add(Paragraph("Nama Penerima")))
        table.addCell(Cell().add(Paragraph(namaPenerima)))
        table.addCell(Cell().add(Paragraph("Barang")))
        table.addCell(Cell().add(Paragraph(desBarang)))
        table.addCell(Cell().add(Paragraph("Kota Pengirim")))
        table.addCell(Cell().add(Paragraph(kotaAsal)))
        table.addCell(Cell().add(Paragraph("Kota Penerima")))
        table.addCell(Cell().add(Paragraph(kotaTujuan)))
        table.addCell(Cell().add(Paragraph("Alamat Lengkap Penerima")))
        table.addCell(Cell().add(Paragraph(alamatLengkap)))
        table.addCell(Cell().add(Paragraph("Biaya")))
        table.addCell(Cell().add(Paragraph(biaya.toString())))

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Dokumen PDF dibuat")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Waktu Pembuatan Dokumen")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        //pembuatan WR CODE secara generate dengan bantuan IText7
        val barcodeQRCode = BarcodeQRCode(
            """
                                        $namaPengirim
                                        $namaPenerima
                                        $desBarang
                                        $kotaAsal
                                        $kotaTujuan
                                        $alamatLengkap
                                        $biaya
                                        ${LocalDate.now().format(dateTimeFormatter)}
                                        ${LocalTime.now().format(timeFormatter)}
                                        """.trimIndent())

        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namaPengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Prf Created", Toast.LENGTH_LONG).show()
    }

    fun getPengiriman(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getData(token_auth,idPengiriman.toString()).enqueue(object : Callback<ResponseDataPengiriman> {
            override fun onResponse(
                call: Call<ResponseDataPengiriman>,
                response: Response<ResponseDataPengiriman>
            ){
                if (response.isSuccessful){
                    listPengiriman.clear()
                    response.body()?.let { listPengiriman.addAll(it.data) }

                    if (listPengiriman.size == 1){
                        binding.inputPengirim.getEditText()?.setText(listPengiriman[0].namaPengirim)
                        binding.autoCompleteNamaPenerima.setText(listPengiriman[0].namaPenerima, false)
                        binding.autoCompleteIsiKiriman.setText(listPengiriman[0].desBarang, false)
                        binding.autoCompleteKotaPengirim.setText(listPengiriman[0].kotaAsal, false)
                        binding.autoCompleteKotaPenerima.setText(listPengiriman[0].kotaTujuan, false)
                        binding.inputAlamatLengkap.getEditText()?.setText(listPengiriman[0].alamatLengkap)
                    }else{
                        for (i in listPengiriman){
                            if (i.idP == idPengiriman){
                                binding.inputPengirim.getEditText()?.setText(i.namaPengirim)
                                binding.autoCompleteNamaPenerima.setText(i.namaPenerima, false)
                                binding.autoCompleteIsiKiriman.setText(i.desBarang, false)
                                binding.autoCompleteKotaPengirim.setText(i.kotaAsal, false)
                                binding.autoCompleteKotaPenerima.setText(i.kotaTujuan, false)
                                binding.inputAlamatLengkap.getEditText()?.setText(i.alamatLengkap)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataPengiriman>, t: Throwable) {}
        })
    }

    private fun getList(){
        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.getDataPenerima(token_auth,"").enqueue(object : Callback<ResponseDataPenerima>{
            override fun onResponse(
                call: Call<ResponseDataPenerima>,
                response: Response<ResponseDataPenerima>
            ) {
                if (response.isSuccessful){
                    listPenerima.clear()
                    response.body()?.let { listPenerima.addAll(it.data) }
                    if (listPenerima.size > 0){
                        arrayNamaPenerima.clear()
                        for (item in listPenerima){
                            arrayNamaPenerima.add(item.nama_penerima)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataPenerima>, t: Throwable) {}
        })

        server.instances.getDataKiriman(token_auth, "").enqueue(object : Callback<ResponseDataKiriman>{
            override fun onResponse(
                call: Call<ResponseDataKiriman>,
                response: Response<ResponseDataKiriman>
            ) {
                if (response.isSuccessful){
                    listKiriman.clear()
                    response.body()?.let { listKiriman.addAll(it.data) }
                    if (listKiriman.size > 0){
                        arrayNamaKiriman.clear()
                        for (item in listKiriman){
                            arrayNamaKiriman.add(item.namaBar)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataKiriman>, t: Throwable) {
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    //testing to move fragment
//    fun loadFragment(){
//        val intent = Intent(this@EditActivity, DeliveryFragment::class.java)
//        startActivity(intent)
//    }
}