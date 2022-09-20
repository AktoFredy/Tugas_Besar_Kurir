package com.example.tubes1

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var username_rgs: TextInputLayout
    private lateinit var password_rgs: TextInputLayout
    private lateinit var confirmPass_rgs: TextInputLayout
    private lateinit var email_rgs: TextInputLayout
    private lateinit var tglLahir_rgs: TextInputLayout
    private lateinit var tglEditText : EditText
    private lateinit var noTlp_rgs: TextInputLayout
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        username_rgs = findViewById(R.id.input_username)
        password_rgs = findViewById(R.id.input_password1)
        confirmPass_rgs = findViewById(R.id.input_confirm_password)
        email_rgs = findViewById(R.id.input_email)
        tglLahir_rgs = findViewById(R.id.input_tanggal_lahir)
        tglEditText =  findViewById(R.id.tglEdt)

        noTlp_rgs = findViewById(R.id.input_tlp)
        val btnBack_ly1: ImageView = findViewById(R.id.btnBack_ly1)
        val btnRegister_click: Button = findViewById(R.id.btnRegis)
        val sign_in_click: Button = findViewById(R.id.login_click)
        val move_to_login2 = Intent(this, LoginActivity::class.java)
        val regBundle = Bundle()
        regBundle.putString("username", "")
        regBundle.putString("password", "")
        regBundle.putString("email", "")
        regBundle.putString("tglLahir", "")
        regBundle.putString("tlp", "")
        move_to_login2.putExtra("register", regBundle)

        // untuk datePicker
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        tglEditText.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        sign_in_click.setOnClickListener {
            val move_to_login1 = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(move_to_login2)
        }

        btnBack_ly1.setOnClickListener{
            val move_to_main1 = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(move_to_main1)
        }

        btnRegister_click.setOnClickListener (View.OnClickListener{
            var checkRegister = false
            val temp_username: String = username_rgs.getEditText()?.getText().toString()
            val temp_password: String = password_rgs.getEditText()?.getText().toString()
            val temp_confirmPass: String = confirmPass_rgs.getEditText()?.getText().toString()
            val temp_email: String = email_rgs.getEditText()?.getText().toString()
            val temp_tglLahir: String = tglLahir_rgs.getEditText()?.getText().toString()
            val temp_noTlp: String = noTlp_rgs.getEditText()?.getText().toString()

            if(temp_username.isEmpty()){
                username_rgs.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_password.isEmpty()){
                password_rgs.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_confirmPass.isEmpty()){
                confirmPass_rgs.setError("Password Confirm Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_password != temp_confirmPass){
                confirmPass_rgs.setError("Password Confirm Harus Sama")
                checkRegister = false
            }

            if(temp_email.isEmpty()){
                email_rgs.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_tglLahir.isEmpty()){
                tglLahir_rgs.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(temp_noTlp.isEmpty()){
                noTlp_rgs.setError("No Tlp Tidak Boleh Kosong")
                checkRegister = false
            }else if(temp_noTlp.length < 12){
                noTlp_rgs.setError("Panjang No Tlp harus > 12")
                checkRegister = false
            }

            if(!temp_username.isEmpty() && !temp_password.isEmpty() && !temp_confirmPass.isEmpty() && !temp_email.isEmpty() && !temp_tglLahir.isEmpty() && !temp_noTlp.isEmpty() && temp_noTlp.length == 12 && temp_password.equals(temp_confirmPass)){
                checkRegister = true
                regBundle.putString("username", temp_username)
                regBundle.putString("password", temp_password)
                regBundle.putString("email", temp_email)
                regBundle.putString("tglLahir", temp_tglLahir)
                regBundle.putString("tlp", temp_noTlp)
                move_to_login2.putExtra("register", regBundle)
            }

            if(!checkRegister)return@OnClickListener
            startActivity(move_to_login2)
        })
    }
    private fun updateEditText(){
        var temp : String
        val formatku = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(formatku, Locale.US)
        temp = sdf.format(cal.getTime())
        tglLahir_rgs.getEditText()?.setText(temp)
    }
}