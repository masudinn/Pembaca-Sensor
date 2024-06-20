package com.instiper.myapplication.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.instiper.myapplication.databinding.FragmentProfileBinding
import com.instiper.myapplication.model.UserModel

class ProfileFragment : Fragment() {

    private var binding : FragmentProfileBinding? = null
    private val REQUEST_GALLERY = 1001
    private var dp : String? = null
    private var model : UserModel? = null
    private val user = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding?.imgProfile?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_GALLERY)
        }

        binding?.updateProfile?.setOnClickListener {
            updateProfile()
        }

        getUserLogin()

        return binding?.root
    }

    private fun updateProfile() {
        val username = binding?.tvUsername?.text.toString().trim()
        val alamat = binding?.tvAlamat?.text.toString().trim()
        val email = binding?.tvEmail?.text.toString().trim()

        when {
            username.isEmpty() -> {
                Toast.makeText(context, "Username jangan kosong", Toast.LENGTH_SHORT).show()
            }
            alamat.isEmpty() -> {
                Toast.makeText(context, "Alamat jangan kosong", Toast.LENGTH_SHORT).show()
            }
            email.isEmpty() -> {
                Toast.makeText(context, "Email jangan kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding?.pbProfile?.visibility = View.VISIBLE
                if(dp == null){
                    val data = mapOf(
                        "username" to username,
                        "alamat" to alamat,
                        "email" to email
                    )
                    user.let { it->
                        FirebaseFirestore
                            .getInstance()
                            .collection("users")
                            .document(it)
                            .update(data)
                            .addOnCompleteListener { data->
                                if(data.isSuccessful){
                                    binding?.pbProfile?.visibility = View.GONE
                                    Toast.makeText(context, "Update profile berhasil", Toast.LENGTH_SHORT).show()
                                }else{
                                    binding?.pbProfile?.visibility = View.GONE
                                    Toast.makeText(context, "Update profile gagal, cek koneksi anda", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }else{
                    val data = mapOf(
                        "username" to username,
                        "alamat" to alamat,
                        "email" to email,
                        "image" to dp
                    )
                    user.let { it->
                        binding?.pbProfile?.visibility = View.VISIBLE
                        FirebaseFirestore
                            .getInstance()
                            .collection("users")
                            .document(it)
                            .update(data)
                            .addOnCompleteListener { data ->
                                if(data.isSuccessful){
                                    binding?.pbProfile?.visibility = View.GONE
                                    Toast.makeText(context, "Update profile berhasil", Toast.LENGTH_SHORT).show()
                                }else{
                                    binding?.pbProfile?.visibility = View.GONE
                                    Toast.makeText(context, "Update profile gagal, cek koneksi anda", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun getUserLogin() {
        binding?.pbProfile?.visibility = View.VISIBLE
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(user)
            .get()
            .addOnSuccessListener { documentSnapshot : DocumentSnapshot ->
                binding?.pbProfile?.visibility = View.GONE
                val username = "" + documentSnapshot["username"].toString()
                val email = "" + documentSnapshot["email"].toString()
                val alamat = "" + documentSnapshot["alamat"].toString()
                val image = "" + documentSnapshot["image"].toString()
                val password = "" + documentSnapshot["password"].toString()
                binding!!.tvUsername.setText(username)
                binding!!.tvAlamat.setText(alamat)
                binding!!.tvEmail.setText(email)

                Glide.with(requireContext())
                    .load(image)
                    .into(binding!!.imgProfile)

                Toast.makeText(context, password, Toast.LENGTH_SHORT).show()
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                uploadDevicesImage(data?.data)
            }
        }
    }

    private fun uploadDevicesImage(data: Uri?) {
        val storageRef = FirebaseStorage.getInstance().reference
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Mohon ditunggu terlebih dahulu...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        val imageFileName = "users/image_" + System.currentTimeMillis().toString()
        storageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                storageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        progressDialog.dismiss()
                        dp = uri.toString()
                        Glide.with(requireContext())
                            .load(dp)
                            .into(binding!!.imgProfile)
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }

}