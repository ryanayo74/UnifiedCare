package Adapter

import ModelClass.Facility
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

class FacilityAdapter(private val facilities: List<Facility>) :
    RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.facility_item, parent, false)
        return FacilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val facility = facilities[position]
        holder.bind(facility)
    }

    override fun getItemCount(): Int {
        return facilities.size
    }

    inner class FacilityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val facilityName: TextView = itemView.findViewById(R.id.facilityname)
        private val facilityDescription: TextView = itemView.findViewById(R.id.facilityDescription)
        private val facilityImage: ImageView = itemView.findViewById(R.id.listImage)
        private val facilityRating: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val favoriteButton: ToggleButton = itemView.findViewById(R.id.heartbtn)

        fun bind(facility: Facility) {
            facilityName.text = facility.name
            facilityDescription.text = facility.description
            facilityRating.rating = facility.rating

            // Use Glide to load the image from a URL
            Glide.with(itemView.context)
                .load(facility.imageUrl)
                .placeholder(R.drawable.placeholder_image) // Optional placeholder
                .into(facilityImage)

            favoriteButton.isChecked = facility.isFavorite
            favoriteButton.setOnCheckedChangeListener { _, isChecked ->
                facility.isFavorite = isChecked
            }
        }
    }
}
