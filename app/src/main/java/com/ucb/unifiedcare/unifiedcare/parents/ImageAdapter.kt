package com.ucb.unifiedcare.unifiedcare.parents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ucb.unifiedcare.R

class ImageAdapter (private val imageList: List<Int>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.itemView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.itemView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        holder.imageView.setImageResource(imageList[position])
    }
//comment ni sha
    override fun getItemCount(): Int {
        return imageList.size
    }
}