package com.instiper.myapplication.ui.chart

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.instiper.myapplication.R

class ChartMarkerView (context: Context?, private val lineChart: LineChart,private val lineChart2: LineChart,private val lineChart3: LineChart, layoutResource: Int, axisX: AxisDateFormatter, axisX2: AxisDateFormatter) : MarkerView(context, layoutResource), IMarker {
//    private val square: TextView
//    private val item: TextView
    private val Title: TextView
    private val Title2: TextView
    private val XAxis: AxisDateFormatter
    private val XAxis2: AxisDateFormatter

    init {
//        square = findViewById(R.id.square1)
//        item = findViewById(R.id.item1)
        Title = findViewById(R.id.txtTitle)
        Title2 = findViewById(R.id.txtTitle2)
        XAxis = axisX
        XAxis2 = axisX2
    }

    override fun refreshContent(e: Entry, highlight: Highlight) {
        try {
            Title.text = XAxis.getFormattedValue(e.x).toString()
            Title2.text = XAxis2.getFormattedValue(e.x).toString()
//            square.setBackgroundColor(lineChart.data.getDataSetByIndex(0).color)
//            square.setBackgroundColor(lineChart2.data.getDataSetByIndex(0).color)
//            square.setBackgroundColor(lineChart3.data.getDataSetByIndex(0).color)
//            val val1 = lineChart.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//            val val2 = lineChart2.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//            val val3 = lineChart3.data.getDataSetByIndex(2).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//            item.text = String.format("%,.0f", val1.y)
//            item.text = String.format("%,.0f", val2.y)
//            item.text = String.format("%,.0f", val3.y)

//            if(lineChart.data.getDataSetByIndex(0).equals(true)){
//                square.setBackgroundColor(lineChart.data.getDataSetByIndex(0).color)
//                val val1 = lineChart.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//                item.text = String.format("%,.0f", val1.y)
//            }
//            if(lineChart2.data.getDataSetByIndex(0).equals(true)){
//                square.setBackgroundColor(lineChart2.data.getDataSetByIndex(0).color)
//                val val2 = lineChart2.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//                item.text = String.format("%,.0f", val2.y)
//            }
//            if(lineChart3.data.getDataSetByIndex(0).equals(true)){
//                square.setBackgroundColor(lineChart3.data.getDataSetByIndex(0).color)
//                val val3 = lineChart3.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
//                item.text = String.format("%,.0f", val3.y)
//            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }
}

class AxisDateFormatter(private val mValues: Array<String>) :  ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value >= 0) {
            if (mValues.size > value.toInt()) {
                mValues[value.toInt()]
            } else ""
        } else {
            ""
        }
    }
}