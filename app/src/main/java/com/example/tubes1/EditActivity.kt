package com.example.tubes1

import android.annotation.SuppressLint
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
import com.example.tubes1.client.server
import com.example.tubes1.databinding.ActivityEditBinding
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
    private val listPengiriman = ArrayList<PengirimanData>()
    private lateinit var prefManager: PrefManager

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
        val kotaPenerima = resources.getStringArray(R.array.kotaPenerima)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, kotaPenerima)
        binding.autoCompleteKotaPenerima.setAdapter(arrayAdapter)
        binding.autoCompleteKotaPengirim.setAdapter(arrayAdapter)
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
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                    if (namaPengirim.isEmpty() || namaPenerima.isEmpty() || desBarang.isEmpty() || kotaAsal.isEmpty() || kotaTujuan.isEmpty() || alamatLengkap.isEmpty()){
                                        Toast.makeText(applicationContext, "Semua Field Tidak Ada yang Boleh Kosong", Toast.LENGTH_LONG).show()
                                    }else{
                                        createPdf(namaPengirim, namaPenerima, desBarang, kotaAsal, kotaTujuan, alamatLengkap, Biaya)
                                    }
                                }
                            }catch (e: FileNotFoundException){
                                e.printStackTrace()
                            }

                            // finish saving to database
                            Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                            finish()
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
        // ini berguna untuk akses writing ke storage hp kalian dalam model download
        //harus diketik jangan COPAS
        var id = 1;
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Pdforder" + id + ".pdf")
        FileOutputStream(file)
        id++
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
        val namaPengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        Berikut adalah 
                        Nama Pengguna UAJY 2022/2023
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
                        binding.inputPenerima.getEditText()?.setText(listPengiriman[0].namaPenerima)
                        binding.inputIsiKiriman.getEditText()?.setText(listPengiriman[0].desBarang)
                        binding.inputKotaPengirim.autoCompleteKotaPengirim.setText(listPengiriman[0].kotaAsal, false)
                        binding.inputKotaPenerima.autoCompleteKotaPenerima.setText(listPengiriman[0].kotaTujuan, false)
                        binding.inputAlamatLengkap.getEditText()?.setText(listPengiriman[0].alamatLengkap)
                    }else{
                        for (i in listPengiriman){
                            if (i.idP == idPengiriman){
                                binding.inputPengirim.getEditText()?.setText(i.namaPengirim)
                                binding.inputPenerima.getEditText()?.setText(i.namaPenerima)
                                binding.inputIsiKiriman.getEditText()?.setText(i.desBarang)
                                binding.inputKotaPengirim.autoCompleteKotaPengirim.setText(i.kotaAsal, false)
                                binding.inputKotaPenerima.autoCompleteKotaPenerima.setText(i.kotaTujuan, false)
                                binding.inputAlamatLengkap.getEditText()?.setText(i.alamatLengkap)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataPengiriman>, t: Throwable) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}