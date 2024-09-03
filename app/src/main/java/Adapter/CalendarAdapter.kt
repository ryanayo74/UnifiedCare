package Adapter

import Utilities.CalendarUtils
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import com.ucb.unifiedcare.R

class CalendarAdapter(
    private val days: ArrayList<LocalDate?>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = if (days.size > 15) { // month view
            (parent.height * 0.166666666).toInt()
        } else { // week view
            parent.height
        }
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        if (date == null) {
            holder.dayOfMonth.text = ""
        } else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == CalendarUtils.selectedDate) {
                holder.parentView.setBackgroundColor(Color.LTGRAY)
            }
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}
