package com.instiper.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.instiper.myapplication.databinding.AdapterSuhuBinding
import com.instiper.myapplication.model.SuhuModel

class SuhuAdapter : RecyclerView.Adapter<SuhuAdapter.ViewHolder>() {

    private val suhuList = ArrayList<SuhuModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<SuhuModel>) {
        suhuList.clear()
        suhuList.addAll(items)
        suhuList.reverse()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding : AdapterSuhuBinding)
        : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(suhuModel: SuhuModel) {
            with(binding) {
                tvCelcius.text = "${suhuModel.suhu}"
                tvFarenhit.text = "${suhuModel.ph}"
                tvKelembapan.text = "${suhuModel.tds} PPM"
                tvTime.text = "${suhuModel.tanggal} | ${suhuModel.jam}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSuhuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = suhuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(suhuList[position])
    }
}