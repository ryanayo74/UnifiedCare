package Adapter

import ModelClass.Appointment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ucb.unifiedcare.R

class AppointmentsAdapter(
    private val appointments: List<Appointment>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onViewClick(appointment: Appointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.parentName.text = appointment.parentName
        holder.timeSlot.text = appointment.timeSlot

        // Handle View button click
        holder.viewText.setOnClickListener {
            listener.onViewClick(appointment)
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentName: TextView = itemView.findViewById(R.id.parentName)
        val timeSlot: TextView = itemView.findViewById(R.id.timeSlot)
        val viewText: TextView = itemView.findViewById(R.id.viewDetails)
    }
}
