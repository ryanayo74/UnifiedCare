package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.CalendarAdapter
import Utilities.CalendarUtils
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import com.ucb.unifiedcare.R
import com.jakewharton.threetenabp.AndroidThreeTen

class ParentsChildsProgressPageActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_parents_childs_progress_page)
        initWidgets()
        CalendarUtils.selectedDate = LocalDate.now()
        setMonthView()
    }
    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }
    private fun setMonthView() {
        monthYearText.text = CalendarUtils.selectedDate?.let { CalendarUtils.monthYearFromDate(it) }
        val daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate)

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }
    fun previousMonthAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.minusMonths(1)
        setMonthView()
    }
    fun nextMonthAction(view: View) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.plusMonths(1)
        setMonthView()
    }
    override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date
        setMonthView()
    }

    fun weeklyAction(view: View) {
        startActivity(Intent(this, WeekViewActivity::class.java))
    }
}