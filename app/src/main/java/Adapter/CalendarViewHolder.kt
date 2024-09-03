package Adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import com.ucb.unifiedcare.R

class CalendarViewHolder(
    itemView: View,
    private val onItemListener: CalendarAdapter.OnItemListener,
    private val days: ArrayList<LocalDate?>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val parentView: View = itemView.findViewById(R.id.parentView)
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}