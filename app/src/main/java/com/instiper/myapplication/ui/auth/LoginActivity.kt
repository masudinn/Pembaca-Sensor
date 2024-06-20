package com.instiper.myapplication.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.instiper.myapplication.R
import com.instiper.myapplication.databinding.ActivityLoginBinding
import com.instiper.myapplication.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    private var binding : ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        autoLogin()

        binding?.loginBtn?.setOnClickListener {
            formValidation()
        }

        binding?.btnRegister?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun autoLogin() {
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun formValidation() {
        val username = binding?.tvUsername?.text.toString().trim()
        val password = binding?.tvPassword?.text.toString().trim()

        if(username.isEmpty()){
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else if(password.length > 8 && password.length == 9 ){
            Toast.makeText(this, "Password harus berjumlah 8 karakter atau kurang", Toast.LENGTH_SHORT).show()
        }else{
            binding?.pbLogin?.visibility = View.VISIBLE
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener {documents->
                    if(documents.size() > 0){
                        for(document in documents){
                            val email = "" + document.data["email"]
                            FirebaseAuth
                                .getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                    if(it.isSuccessful){
                                        binding?.pbLogin?.visibility = View.GONE
                                        Toast.makeText(this, "Selamat datang di apps monitoring akuaponik", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }else{
                                        binding?.pbLogin?.visibility = View.GONE
                                        Toast.makeText(this, "Pastikan username dan password anda benar", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }else{
                        binding?.pbLogin?.visibility = View.GONE
                        failureDialog()
                    }
                }
        }
    }

    private fun failureDialog() {
            AlertDialog
                .Builder(this)
                .setTitle("Gagal Login")
                .setMessage("Cek koneksi internet anda dan pastikan jaringan anda stabil")
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