package com.instiper.myapplication.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.instiper.myapplication.adapter.SuhuAdapter
import com.instiper.myapplication.databinding.ActivityHomeDetailBinding
import com.instiper.myapplication.ui.chart.ChartActivity
import com.instiper.myapplication.viewmodel.SuhuViewModel

class HomeDetailActivity : AppCompatActivity() {

    private var binding : ActivityHomeDetailBinding? = null
    private var adapter : SuhuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.swipe?.setOnRefreshListener {
            initRecyclerView()
            initViewModel()
            binding?.swipe?.isRefreshing = false
        }

        binding?.chart?.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }
    }


    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[SuhuViewModel::class.java]
        binding?.pbSuhu?.visibility = View.VISIBLE
        viewModel.setListSuhu()
        viewModel.getListSuhu().observe(this) { productList ->
            if (productList.size > 0) {
                binding?.pbSuhu?.visibility = View.GONE
                adapter?.setData(productList)
            } else {
                binding?.pbSuhu?.visibility = View.VISIBLE
            }
            binding?.pbSuhu?.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        binding?.rvSuhu?.layoutManager = LinearLayoutManager(this)
        adapter = SuhuAdapter()
        binding?.rvSuhu?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}