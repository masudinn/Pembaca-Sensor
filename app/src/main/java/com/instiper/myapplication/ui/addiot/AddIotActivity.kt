package com.instiper.myapplication.ui.addiot

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.instiper.myapplication.R
import com.instiper.myapplication.databinding.ActivityAddIotBinding
import com.instiper.myapplication.databinding.FragmentAddIotBinding
import com.instiper.myapplication.ui.home.HomeFragment

class AddIotActivity : AppCompatActivity() {

    private var binding: ActivityAddIotBinding? = null
    private var dp: String? = null
    private val REQUEST_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIotBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.uploadBtn?.setOnClickListener {
            formValidation()
        }

        binding?.imageHint?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_GALLERY)
        }
    }

    private fun formValidation() {
        val idDevices = binding?.tvId?.text.toString().trim()
        val namaDevices = binding?.tvName?.text.toString().trim()
        val lokasiDevices = binding?.tvLokasi?.text.toString().trim()


        when {
            idDevices.isEmpty() -> {
                Toast.makeText(this, "Id devices jangan kosong", Toast.LENGTH_SHORT).show()
            }
            idDevices.length > 3 -> {
                Toast.makeText(this, "Id devices 3 karakter saja, tidak lebih", Toast.LENGTH_SHORT)
                    .show()
            }
            namaDevices.isEmpty() -> {
                Toast.makeText(this, "Nama devices jangan kosong", Toast.LENGTH_SHORT).show()
            }
            lokasiDevices.isEmpty() -> {
                Toast.makeText(this, "Lokasi jangan kosong", Toast.LENGTH_SHORT).show()
            }
            dp == null -> {
                Toast.makeText(this, "Gambar devices jangan kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding?.pbAddIot?.visibility = View.VISIBLE
                val devicesId = System.currentTimeMillis().toString()
                val data = mapOf(
                    "idDevices" to idDevices,
                    "namaDevices" to namaDevices,
                    "lokasiDevices" to lokasiDevices,
                    "foto" to dp
                )
                FirebaseFirestore
                    .getInstance()
                    .collection("a02")
                    .document(devicesId)
                    .set(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding?.pbAddIot?.visibility = View.GONE
//                            Toast.makeText(context, "Sukses menambahkan alat baru", Toast.LENGTH_SHORT).show()
                            showDialogSuccess()
                        } else {
                            binding?.pbAddIot?.visibility = View.GONE
//                            Toast.makeText(context, "Gagal menambahkan alat baru", Toast.LENGTH_SHORT).show()
                            showDialogFail()
                        }
                    }
            }
        }
    }

    private fun showDialogFail() {
        AlertDialog
            .Builder(this)
            .setTitle("Gagal menambahkan alat")
            .setMessage("Cek koneksi internet anda dan jaringan anda stabil")
            .setIcon(R.drawable.baseline_warning_24)
            .setPositiveButton("Oke") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }


    private fun showDialogSuccess() {
        AlertDialog
            .Builder(this)
            .setTitle("Berhasil menambahkan alat")
            .setMessage("Mohon menunggu tim IT untuk pengecekan alat yang anda tambahkan")
            .setIcon(R.drawable.baseline_cloud_done_24)
            .setPositiveButton("Oke") { _, _ ->
                onBackPressed()
            }
            .show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                uploadDevicesImage(data?.data)
            }
        }
    }

    private fun uploadDevicesImage(data: Uri?) {
        val storageRef = FirebaseStorage.getInstance().reference
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mohon ditunggu terlebih dahulu...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        val imageFileName = "devices/image_" + System.currentTimeMillis().toString()
        storageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                storageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        progressDialog.dismiss()
                        dp = uri.toString()
                        Glide.with(this)
                            .load(dp)
                            .into(binding!!.imageHint)
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Gagal mengunggah gambar!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}