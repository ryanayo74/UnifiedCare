package com.ucb.unifiedcare.unifiedcare.parents

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ucb.unifiedcare.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ParentsChildsProgressPageProgressSummaryActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var barArrayList: ArrayList<BarEntry>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_childs_progress_page_progress_summary)

        // Initialize the BarChart
        barChart = findViewById(R.id.barChart)

        // Populate the data
        getData()

        // Create a BarDataSet with the data
        val barDataSet = BarDataSet(barArrayList, "Cambo Tutorial")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f

        // Create BarData and set it to the chart
        val barData = BarData(barDataSet)
        barChart.data = barData

        // Refresh the chart
        barChart.invalidate() // or barChart.notifyDataSetChanged()
    }
    private fun getData() {
        barArrayList = ArrayList()

        barArrayList.add(BarEntry(2f, 10f))
        barArrayList.add(BarEntry(3f, 20f))
        barArrayList.add(BarEntry(4f, 30f))
        barArrayList.add(BarEntry(5f, 40f))
        barArrayList.add(BarEntry(6f, 50f))
    }

}