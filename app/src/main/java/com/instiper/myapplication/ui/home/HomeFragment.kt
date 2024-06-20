package com.instiper.myapplication.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.instiper.myapplication.R
import com.instiper.myapplication.adapter.SuhuAdapter
import com.instiper.myapplication.databinding.FragmentHomeBinding
import com.instiper.myapplication.ui.addiot.AddIotActivity
import com.instiper.myapplication.viewmodel.SuhuViewModel

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.satu?.setOnClickListener {
            val dialog = context?.let { it1 -> Dialog(it1) }
            val bindingDialog = layoutInflater.inflate(R.layout.dialog, null)
            dialog?.setContentView(bindingDialog)

            val btncancel = bindingDialog.findViewById<ImageView>(R.id.btn_cancel)
            btncancel.setOnClickListener {
                dialog?.dismiss()
            }

            val id = bindingDialog.findViewById<EditText>(R.id.textInputLayout)

            var btnSubmit = bindingDialog.findViewById<Button>(R.id.btn_submit)
            btnSubmit.setOnClickListener {
                val value = id.text.toString().trim()
                if(value == "a01"){
                    startActivity(Intent(context, HomeDetailActivity::class.java))
                    dialog?.dismiss()
                }else if(value.isEmpty()){
                    Toast.makeText(context, "Masukkan ID devices", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Kode yang anda masukan tidak sesuai", Toast.LENGTH_SHORT).show()
                }
            }

            dialog?.setCancelable(false)
            dialog?.show()
        }

        binding?.dua?.setOnClickListener {
            Toast.makeText(context, "Hanya tampilan", Toast.LENGTH_SHORT).show()
        }

        binding?.addDevices?.setOnClickListener {
            startActivity(Intent(context, AddIotActivity::class.java))
        }
    }

}