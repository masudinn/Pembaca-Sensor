package com.instiper.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.instiper.myapplication.model.SuhuModel

class SuhuViewModel : ViewModel() {
    private val suhuList = MutableLiveData<ArrayList<SuhuModel>>()
    private val listData = ArrayList<SuhuModel>()

    fun setListSuhu() {
        listData.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("Aquaponik1")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = SuhuModel()
                        model.ph = document.data["ph"].toString()
                        model.suhu = document.data["suhu"].toString().toFloat()
                        model.tds = document.data["tds"].toString()
                        model.jam = document.data["jam"].toString()
                        model.tanggal = document.data["tanggal"].toString()
                        listData.add(model)
                    }
                    suhuList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w("MainActivity", "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getListSuhu() : LiveData<ArrayList<SuhuModel>> {
        return suhuList
    }

}