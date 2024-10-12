package Adapter


import ModelClass.Facility
import ModelClass.Therapist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ucb.unifiedcare.R

class TherapistAdapter(private val therapists: List<Therapist>, private val onItemClick: (Therapist) -> Unit) :
    RecyclerView.Adapter<TherapistAdapter.TherapistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.therapists_list_item, parent, false)
        return TherapistViewHolder(view)
    }

    override fun onBindViewHolder(holder: TherapistViewHolder, position: Int) {
        val therapist = therapists[position]
        holder.bind(therapist)
    }

    override fun getItemCount(): Int {
        return therapists.size
    }

    inner class TherapistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val fullName: TextView = itemView.findViewById(R.id.therapist_name)
    //  private val therapistImage: ImageView = itemView.findViewById(R.id.therapist_image)
        private val therapistType: TextView = itemView.findViewById(R.id.therapy_type)

        fun bind(therapists: Therapist) {
            fullName.text = therapists.firstName
            therapistType.text = therapists.therapyType

            // Use Glide to load the image from a URL
//            Glide.with(itemView.context)
//                .load(therapists.imageUrl)
//                .placeholder(R.drawable.placeholder_image) // Optional placeholder
//                .into(therapistImage)

//            favoriteButton.isChecked = facility.isFavorite
//            favoriteButton.setOnCheckedChangeListener { _, isChecked ->
//                facility.isFavorite = isChecked
            itemView.setOnClickListener {
                onItemClick(therapists) // Call the listener with the clicked facility
            }
        }
    }
}
