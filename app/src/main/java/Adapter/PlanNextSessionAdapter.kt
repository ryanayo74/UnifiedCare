package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ucb.unifiedcare.R

class PlanNextSessionAdapter(
    private val context: Context,
    private val planSessionTitles: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = planSessionTitles.size

    override fun getItem(position: Int): Any = planSessionTitles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.plan_next_session_list, parent, false
        )

        // Get the session title TextView and set the session title
        val sessionTitleTextView = view.findViewById<TextView>(R.id.session_text)
        sessionTitleTextView.text = planSessionTitles[position]

        return view
    }
}