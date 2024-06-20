package com.instiper.myapplication.ui.chart

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.size
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import com.instiper.myapplication.R
import com.instiper.myapplication.adapter.SuhuAdapter
import com.instiper.myapplication.databinding.ActivityChartBinding
import com.instiper.myapplication.model.SuhuModel

class ChartActivity : AppCompatActivity() {

    private var binding : ActivityChartBinding? = null
    private val suhu = ArrayList<Entry>()
    private val tds = ArrayList<Entry>()
    private val ph = ArrayList<Entry>()
    private val tanggal = ArrayList<String>()
    private val jam = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding?.root)

        binding?.swipechart?.setOnRefreshListener {
            getCelcius()
            binding?.swipechart?.isRefreshing = false
        }

    }

    override fun onStart() {
        super.onStart()
        binding?.pbSuhu?.visibility = View.VISIBLE
        getCelcius()
    }

    private fun getCelcius() {
        FirebaseFirestore
            .getInstance()
            .collection("Aquaponik1")
            .get()
            .addOnSuccessListener {
                binding?.pbSuhu?.visibility = View.VISIBLE
                if(it.size() > 0){
                    binding?.pbSuhu?.visibility = View.GONE
                    suhu.clear()
                    ph.clear()
                    tds.clear()
                    var index:Int = -1
                    for(document in it){
                        index ++
                        suhu.add(Entry(index.toFloat(), document.data["suhu"].toString().toFloat()))
                        ph.add(Entry(index.toFloat(), document.data["ph"].toString().toFloat()))
                        tds.add(Entry(index.toFloat(), document.data["tds"].toString().toFloat()))
                        tanggal.add(document.data["tanggal"].toString())
                        jam.add(document.data["jam"].toString())
                    }
                    showChart(suhu, ph, tds , tanggal, jam)
                }
            }.addOnFailureListener { exception ->
                Log.w("MainActivity.kt", "Error getting documents: ", exception)
            }
    }

    private fun showChart(suhu : ArrayList<Entry>, ph : ArrayList<Entry>, tds : ArrayList<Entry>,  tanggal : ArrayList<String>, jam : ArrayList<String>) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val celciusLineDataSet = LineDataSet(suhu, "Suhu")
        celciusLineDataSet.color = Color.RED
        celciusLineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        celciusLineDataSet.valueTextSize = 13f
        celciusLineDataSet.valueTextColor = Color.BLACK
        celciusLineDataSet.setDrawFilled(true)
        celciusLineDataSet.lineWidth = 1f
        celciusLineDataSet.fillColor = Color.RED
        celciusLineDataSet.circleRadius = 2f
        celciusLineDataSet.setCircleColor(Color.RED)

        val phDataSet = LineDataSet(ph, "pH")
        phDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        phDataSet.color = Color.MAGENTA
        phDataSet.valueTextSize = 13f
        phDataSet.valueTextColor = Color.BLACK
        phDataSet.setDrawFilled(true)
        phDataSet.fillColor = Color.MAGENTA
        phDataSet.circleRadius = 2f
        phDataSet.setCircleColor(Color.MAGENTA)

        val tdsDataSet = LineDataSet(tds, "tds")
        tdsDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        tdsDataSet.color = Color.BLUE
        tdsDataSet.valueTextSize = 13f
        tdsDataSet.setDrawFilled(true)
        tdsDataSet.fillColor = Color.BLUE
        tdsDataSet.circleRadius = 2f
        tdsDataSet.setCircleColor(Color.BLUE)

        binding?.lineChart?.setDrawGridBackground(true)
        binding?.lineChart?.setTouchEnabled(true)
        binding?.lineChart?.setPinchZoom(true)


        binding?.lineChart?.isHighlightPerDragEnabled
        binding?.lineChart?.setDrawBorders(true)

        binding?.lineChart2?.setDrawGridBackground(true)
        binding?.lineChart2?.isHighlightPerDragEnabled
        binding?.lineChart2?.setDrawBorders(true)

        binding?.lineChart3?.setDrawGridBackground(true)
        binding?.lineChart3?.setDrawBorders(true)


        val tanggalArray = AxisDateFormatter(tanggal.toArray(arrayOfNulls<String>(tanggal.size)))
        binding?.lineChart?.xAxis?.valueFormatter = tanggalArray
        binding?.lineChart2?.xAxis?.valueFormatter = tanggalArray
        binding?.lineChart3?.xAxis?.valueFormatter = tanggalArray

        val jamArray = AxisDateFormatter(jam.toArray(arrayOfNulls<String>(jam.size)))

        val marker : IMarker = ChartMarkerView(applicationContext, binding!!.lineChart,binding!!.lineChart2, binding!!.lineChart3, R.layout.marker, tanggalArray, jamArray)
        binding?.lineChart?.marker = marker
        binding?.lineChart2?.marker = marker
        binding?.lineChart3?.marker = marker

        binding?.lineChart?.data = LineData(celciusLineDataSet)
        binding?.lineChart2?.data = LineData(phDataSet)
        binding?.lineChart3?.data = LineData(tdsDataSet)
        binding?.lineChart?.animateXY(100, 500)
        binding?.lineChart2?.animateXY(100, 500)
        binding?.lineChart3?.animateXY(100, 500)



        binding?.lineChart?.description?.isEnabled = false
        binding?.lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM

        binding?.lineChart2?.description?.isEnabled = false
        binding?.lineChart2?.xAxis?.position = XAxis.XAxisPosition.BOTTOM

        binding?.lineChart3?.description?.isEnabled = false
        binding?.lineChart3?.xAxis?.position = XAxis.XAxisPosition.BOTTOM

        //Setting Legend
        val legend = binding?.lineChart?.legend
        legend?.isEnabled = true
        legend?.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend?.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend?.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend?.setDrawInside(false)

        val legend2 = binding?.lineChart2?.legend
        legend2?.isEnabled = true
        legend2?.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend2?.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend2?.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend2?.setDrawInside(false)

        val legend3 = binding?.lineChart3?.legend
        legend3?.isEnabled = true
        legend3?.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        legend3?.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend3?.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend3?.setDrawInside(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

}