package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ucb.unifiedcare.R

class RecommendationAdapter(
    private val context: Context,
    private val recommendationTitles: List<String>
) : BaseAdapter() {

    override fun getCount(): Int =  recommendationTitles.size

    override fun getItem(position: Int): Any = recommendationTitles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.recommendation_list, parent, false
        )

        // Get the session title TextView and set the session title
        val recommendationTextView = view.findViewById<TextView>(R.id.session_text)
        recommendationTextView.text =  recommendationTitles[position]

        return view
    }
}