package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.ImageAdapter
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        //buttons
        val call:ImageButton = findViewById(R.id.call_icon)

        val e_mail:ImageButton=findViewById(R.id.email_icon)



        //receive the intent
        val desc = intent.getStringExtra("facilityDesc")
        val address = intent.getStringExtra("address")
        val number = intent.getStringExtra("phone_number")
        val email = intent.getStringExtra("email")

        //display
        val descTextView = findViewById<TextView>(R.id.desc)
        val addressTextView = findViewById<TextView>(R.id.facility_address)
        val phoneNumber = findViewById<TextView>(R.id.phone_number)
        val email_acc = findViewById<TextView>(R.id.email)

        descTextView.text = desc
        addressTextView.text = address
        phoneNumber.text = number
        email_acc.text = email

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

        //listener for the call, email btns

        call.setOnClickListener{
            // Get the phone number from the TextView
            val phoneNum = phoneNumber.text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNum")

           startActivity(intent)
        }
        e_mail.setOnClickListener{
            val emailAdd = email_acc.text.toString()
            sendEmail(emailAdd)
        }

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
    private var selectedTime: String? =null
    private fun showCustomDialog(){
        val dialog = Dialog(this)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.available_date_custom_dialog, null)
        dialog.setContentView(dialogView)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,  // Width
            WindowManager.LayoutParams.WRAP_CONTENT   // Height
        )
        var isSelected = false
        val selectedColor = resources.getColor(R.color.light_pink)
        val defaultColor = resources.getColor(R.color.white)
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
            isSelected = !isSelected
            if (isSelected){
                mondayBtn.setColorFilter(selectedColor)
            }else{
                mondayBtn.setColorFilter(defaultColor)
            }
        }

        tueBtn.setOnClickListener{
            selectedDay = "Tuesday"
            isSelected = !isSelected
            if (isSelected){
                tueBtn.setColorFilter(selectedColor)
            }else{
                tueBtn.setColorFilter(defaultColor)
            }
        }
        wedBtn.setOnClickListener{
            selectedDay = "Wednesday"
            isSelected = !isSelected
            if (isSelected){
                wedBtn.setColorFilter(selectedColor)
            }else{
                wedBtn.setColorFilter(defaultColor)
            }
        }
        thurBtn.setOnClickListener{
            selectedDay = "Thursday"
            isSelected = !isSelected
            if (isSelected){
                thurBtn.setColorFilter(selectedColor)
            }else{
                thurBtn.setColorFilter(defaultColor)
            }
        }
        friBtn.setOnClickListener{
            selectedDay = "Friday"
            isSelected = !isSelected
            if (isSelected){
                friBtn.setColorFilter(selectedColor)
            }else{
                friBtn.setColorFilter(defaultColor)
            }
        }
        satBtn.setOnClickListener{
            selectedDay = "Saturday"
            isSelected = !isSelected
            if (isSelected){
                satBtn.setColorFilter(selectedColor)
            }else{
                satBtn.setColorFilter(defaultColor)
            }
        }
        sunBtn.setOnClickListener{
            selectedDay = "Sunday"
            isSelected = !isSelected
            if (isSelected){
                sunBtn.setColorFilter(selectedColor)
            }else{
                sunBtn.setColorFilter(defaultColor)
            }
        }
        selectBtn.setOnClickListener{
            Log.d("SelectedDay", "Current selected day: $selectedDay")

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

        val window = dialog.window
        window?.setLayout(
             WindowManager.LayoutParams.MATCH_PARENT,  // Width
            WindowManager.LayoutParams.WRAP_CONTENT   // Height
        )
        dialog.show()
    }
    private fun sendEmail(email:String){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto: $email")
            putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        }
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }
}