package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.ImageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.ucb.unifiedcare.R

class SecureSlotActivity : AppCompatActivity() {
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure_slot)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val btn_left: ImageButton = findViewById(R.id.arrowleft)
        val btn_right: ImageButton = findViewById(R.id.arrowright)
        // Example image list (use your own drawables or URLs)
        val images = listOf(
            R.drawable.sample1,
            R.drawable.sample2,
            R.drawable.sample3
        )
        val adapter = ImageAdapter(images)
        viewPager.adapter = adapter

        //handle right arrow
        btn_right.setOnClickListener{
            currentPage = if(currentPage < images.size - 1){
                currentPage + 1
            }else{
                0
            }
            viewPager.setCurrentItem(currentPage, true)
        }
//        //handle left arrow
        btn_left.setOnClickListener{
            currentPage = if(currentPage > 0){
                currentPage - 1
            }else {
                images.size - 1
            }
            viewPager.setCurrentItem(currentPage, true)
        }
    }
}