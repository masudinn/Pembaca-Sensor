package com.instiper.myapplication.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.instiper.myapplication.R
import com.instiper.myapplication.databinding.ActivityRegisterBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RegisterActivity : AppCompatActivity() {

    private var binding : ActivityRegisterBinding? = null
    private var i : Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    val current = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    @RequiresApi(Build.VERSION_CODES.O)
    val formatted = current.format(formatter)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnBack?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding?.btnRegister?.setOnClickListener {
            formValidation()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formValidation() {
        val edt_username = binding?.tvUsername?.text.toString().trim()
        val edt_alamat = binding?.tvAlamat?.text.toString().trim()
        val edt_email = binding?.tvEmail?.text.toString().trim()
        val edt_password1 = binding?.tvPasswordSatu?.text.toString().trim()
        val edt_password2 = binding?.tvPasswordDua?.text.toString().trim()

        if(edt_username.isEmpty()){
            Toast.makeText(this, "Username jangan kosong", Toast.LENGTH_SHORT).show()
        }else if(edt_alamat.isEmpty()){
            Toast.makeText(this, "Alamat jangan kosong", Toast.LENGTH_SHORT).show()
        }else if(edt_email.isEmpty()){
            Toast.makeText(this, "Email jangan kosong", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edt_email).matches()){
            Toast.makeText(this, "Format email anda tidak sesuai", Toast.LENGTH_SHORT).show()
        }else if(edt_password1.isEmpty()){
            Toast.makeText(this, "Passoword jangan kosong", Toast.LENGTH_SHORT).show()
        }else if(edt_password2.isEmpty()){
            Toast.makeText(this, "Repeat password jangan kosong", Toast.LENGTH_SHORT).show()
        }else if(edt_password1 != edt_password2){
            Toast.makeText(this, "Password dan repeat password harus sama", Toast.LENGTH_SHORT).show()
        }else if(edt_password1.length > 8 ){
            Toast.makeText(this, "Password harus berjumlah kurang dari 8 karakter", Toast.LENGTH_SHORT).show()
        }else{
            binding?.pbRegister?.visibility = View.VISIBLE
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(edt_email, edt_password1)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        /// kita simpan data user ke database
                        saveUserToDatabase(edt_username, edt_alamat,  edt_email, edt_password1)
                    } else {
                        /// munculkan peringatan gagal register
                        binding?.pbRegister?.visibility = View.GONE
                        try {
                            throw it.exception!!
                        } catch (e: FirebaseAuthUserCollisionException) {
                            /// jika email sudah didaftarkan sebelumnya, maka user harus pake email lain
                            showDialogFail("Email yang anda daftarkan sudah digunakan, silahkan coba email lain")
                        } catch (e: java.lang.Exception) {
                            Log.e("TAG", e.message!!)
                        }
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserToDatabase(username: String, alamat: String, email: String, password1: String ) {
        val userId = System.currentTimeMillis().toString()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val edtPassword = BCrypt.withDefaults().hashToString(15, password1.toCharArray())
        val data = mapOf(
                "uid" to uid,
                "username" to username,
                "alamat" to alamat,
                "email" to email,
                "password" to edtPassword,
                "image" to null
            )
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .set(data)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        binding?.pbRegister?.visibility = View.GONE
                        showDialogSuccess()
                    }else{
                        binding?.pbRegister?.visibility = View.GONE
                        showDialogFail("Cek koneksi anda, pastikan koneksi anda stabil")
                    }
                }
    }

    private fun showDialogSuccess() {
        AlertDialog
            .Builder(this)
            .setTitle("Berhasil Daftar")
            .setMessage("Selamat anda berhasil melakukan pendaftaran di aplikasi ini")
            .setIcon(R.drawable.baseline_cloud_done_24)
            .setCancelable(false)
            .setPositiveButton("Oke"){ dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }
    private fun showDialogFail(message : String) {
        AlertDialog
            .Builder(this)
            .setTitle("Gagal Daftar")
            .setMessage(message)
            .setIcon(R.drawable.baseline_warning_24)
            .setCancelable(false)
            .setPositiveButton("Oke"){dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}


