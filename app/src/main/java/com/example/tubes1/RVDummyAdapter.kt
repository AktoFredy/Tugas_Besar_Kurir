package com.example.tubes1

//import com.example.tubes1.dataCrud.Pengiriman
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1.client.server
import com.example.tubes1.databinding.RvItemDelivBinding
import com.example.tubes1.fragments.Data_Pengiriman_Fragment
import com.example.tubes1.userSharedPreferences.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RVDummyAdapter(private val pengirimans: ArrayList<PengirimanData>, private val context: Context) : RecyclerView.Adapter<RVDummyAdapter.PengirimanHolder>(){
    inner class PengirimanHolder(item:RvItemDelivBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(pengirimanData: PengirimanData){
            with(binding){
                namaPengirim.text = pengirimanData.namaPengirim
                namaPenerima.text = pengirimanData.namaPenerima
                namaKotaAsal.text = pengirimanData.kotaAsal
                namaKotaTujuan.text = pengirimanData.kotaTujuan
                biaya.text = pengirimanData.ongkos.toString()

                cardView1.setOnClickListener{
                    var i = Intent(context, EditActivity::class.java).apply {
                        putExtra("intent_id", pengirimanData.idP)
                    }
                    context.startActivity(i)
                }
                delBtn.setOnClickListener {
                    deleteItem(pengirimanData.idP)
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    val mfragment = Data_Pengiriman_Fragment()
                    val mfTransaction = manager!!.beginTransaction()
                    val teksCari = ""
                    val mBundle = Bundle()
                    mBundle.putString("cari", teksCari)
                    mfragment.arguments = mBundle
                    mfTransaction.replace(R.id.fl_data, mfragment).commit()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengirimanHolder {
        return PengirimanHolder(RvItemDelivBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PengirimanHolder, position: Int){
        holder.bind(pengirimans[position])
    }

    override fun getItemCount() = pengirimans.size

    fun deleteItem(id: Int){
        val prefManager = PrefManager(context)

        val token_auth = "Bearer ${prefManager.getToken()}"

        server.instances.deleteData(token_auth, id).enqueue(object : Callback<ResponseCreate>{
            override fun onResponse(
                call: Call<ResponseCreate>,
                response: Response<ResponseCreate>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(context.applicationContext, "Data berhasil dihapus", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseCreate>, t: Throwable) { }
        })
    }
}