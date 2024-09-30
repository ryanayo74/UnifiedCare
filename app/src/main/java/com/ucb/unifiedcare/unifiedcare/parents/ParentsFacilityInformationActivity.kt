package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.ImageAdapter
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.ucb.unifiedcare.R

class ParentsFacilityInformationActivity : AppCompatActivity() {
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_facility_information)
        //receive the intent
        val desc = intent.getStringExtra("facilityDesc")

        //display
        val descTextView = findViewById<TextView>(R.id.desc)
        descTextView.text = desc

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val btn_left: ImageButton = findViewById(R.id.arrowleft)
        val btn_right: ImageButton = findViewById(R.id.arrowright)
        val slotBtn: Button = findViewById(R.id.secure_slotsbtn)

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

        slotBtn.setOnClickListener{
            showCustomDialog()
        }

    }
    private var selectedDay: String? =null
    private fun showCustomDialog(){
        val dialog = Dialog(this)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.available_date_custom_dialog, null)
        dialog.setContentView(dialogView)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,  // Width
            WindowManager.LayoutParams.WRAP_CONTENT   // Height
        )
        val selectBtn: Button = dialogView.findViewById(R.id.select_btn)
        val cancelBtn: Button = dialogView.findViewById(R.id.cancel_btn)
        val mondayBtn: ImageButton = dialogView.findViewById(R.id.mon)
        val tueBtn: ImageButton = dialogView.findViewById(R.id.tue)
        val wedBtn: ImageButton = dialogView.findViewById(R.id.wed)
        val thurBtn: ImageButton = dialogView.findViewById(R.id.thur)
        val friBtn: ImageButton = dialogView.findViewById(R.id.fri)
        val satBtn: ImageButton = dialogView.findViewById(R.id.sat)
        val sunBtn: ImageButton = dialogView.findViewById(R.id.sun)

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }
        mondayBtn.setOnClickListener{
            selectedDay = "Monday"
        }
        tueBtn.setOnClickListener{
            selectedDay = "Tuesday"
        }
        wedBtn.setOnClickListener{
            selectedDay = "Wednesday"
        }
        thurBtn.setOnClickListener{
            selectedDay = "Thursday"
        }
        friBtn.setOnClickListener{
            selectedDay = "Friday"
        }
        satBtn.setOnClickListener{
            selectedDay = "Saturday"
        }
        sunBtn.setOnClickListener{
            selectedDay = "Sunday   "
        }
        selectBtn.setOnClickListener{
            if(selectedDay != null){
                dialog.dismiss()
                showCustomTimeDialog()
            }else {
                Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }
    private fun showCustomTimeDialog(){
        val dialog = Dialog(this)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.available_time_custom_dialog, null)
        dialog.setContentView(dialogView)


    }
}