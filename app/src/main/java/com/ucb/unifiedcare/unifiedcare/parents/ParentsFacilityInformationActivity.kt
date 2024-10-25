package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.ImageAdapter
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R

class ParentsFacilityInformationActivity : AppCompatActivity() {
    private var currentPage = 0
    private lateinit var webView: WebView
    var isMapVisible = false
    val db = FirebaseFirestore.getInstance()
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
        val id = intent.getStringExtra("id")

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
            if (id != null) {
                Log.d("ID", "$id")
                showAvailableDaysDialog(id)
            }
        }

        //MAP implementation
        webView = findViewById(R.id.webview)
        // Set up the WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true

        // Load the leaflet map HTML file
        webView.loadUrl("file:///android_asset/leafllet_map.html")

        // Hide map initially
        webView.visibility = View.GONE

        // Show map when EditText is clicked
        addressTextView.setOnClickListener {
            webView.visibility = View.VISIBLE
            isMapVisible = true
        }

        // Handle manual address input
        addressTextView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val userAddress = addressTextView.text.toString().trim()
                if (userAddress.isNotEmpty()) {
                    val encodedAddress = Uri.encode(userAddress)
                    webView.evaluateJavascript("javascript:moveMarkerToAddress('$encodedAddress');", null)
                }
                true
            } else {
                false
            }
        }
        // Add this inside your onCreate method after initializing the WebView
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun updateAddressField(address: String) {
                runOnUiThread {
                    addressTextView.setText(address)  // Update the EditText with the address
                }
            }
        }, "Android")

        // Add this inside your onCreate method after initializing the WebView
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun closeMap() {
                runOnUiThread {
                    webView.visibility = View.GONE  // Hide the WebView (map)
                    isMapVisible = false  // Update the visibility flag
                }
            }
        }, "Android")
        //END OF MAP IMPLEMENTATION
    }

    private var selectedDay: String? =null
    private var selectedTime: String? =null

    //fetch schedule availability from firebase
    fun fetchAvailableSched(facilityId: String, onResult: (Map<String, List<Map<String, String>>>) -> Unit) {
        Log.d("FunctionCall", "fetchAvailableSched called for facilityId: $facilityId")
        db.collection("Users")
            .document("facility")
            .collection("userFacility")
            .document(facilityId)  // Document ID is the same as facilityId (e.g., "FirstFacility")
            .collection("scheduleAvailability")
            .document(facilityId)  // Schedule doc ID matches facilityId
            .get()
            .addOnSuccessListener { document  ->
                if (document .exists()) {
                    Log.d("FirestoreDocument", "Document data: ${document .data}")

                    // Cast the availabilitySchedule to the correct type
                    val availabilitySchedule = document .get("availabilitySchedule") as? Map<String, List<Map<String, String>>>
                    Log.d("AvailabilitySchedule", "Availability schedule: $availabilitySchedule")

                    // Pass the result or an empty map if the casting fails
                    onResult(availabilitySchedule ?: emptyMap())
                } else {
                    Log.w("FirestoreDocument", "No such document")
                    // No document exists, return an empty map
                    onResult(emptyMap())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching available schedule", exception)
                // In case of failure, return an empty map
                onResult(emptyMap())
            }
    }

    //show available days
    private fun showAvailableDaysDialog(facilityId: String){
        fetchAvailableSched(facilityId) {availabilitySchedule->
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

            //button mapping to firebase days label
            val dayButtons = mapOf(
                "Monday" to mondayBtn,
                "Tuesday" to tueBtn,
                "Wednesday" to wedBtn,
                "Thursday" to thurBtn,
                "Friday" to friBtn,
                "Saturday" to satBtn,
                "Sunday" to sunBtn
            )

            //disable buttons for days that have no available time slots
            for((day, button) in dayButtons){
                Log.d("DayCheck", "Checking for day: $day")
                val timeSlots = availabilitySchedule[day]
                Log.d("DayTimeSlots", "Day: $day, TimeSlots: $timeSlots")

                val validTimeSlots = timeSlots?.filter { slot ->
                    val start = slot["start"] ?: ""
                    val end = slot["end"] ?: ""
//                  Log.d("SlotCheck", "Day: $day, Start: $start, End: $end")
                    start.isNotEmpty() && end.isNotEmpty()
                }
                Log.d("ValidTimeSlots", "Day: $day, Valid TimeSlots: $validTimeSlots")

            if (validTimeSlots.isNullOrEmpty()) {
                // Disable button
                button.isEnabled = false
                button.alpha = 0.5f
            } else {
                button.isEnabled = true
                button.alpha = 1.0f
            }
                //set onclick for each enabled button
                button.setOnClickListener{
                    if (button.isEnabled){
                        selectedDay = day
                        isSelected = !isSelected

                        //toggle color
                        if(isSelected){
                            button.setColorFilter(selectedColor)
                        }else{
                            button.setColorFilter(defaultColor)
                        }
                    }
                }
            }
            cancelBtn.setOnClickListener{
                dialog.dismiss()
            }
            selectBtn.setOnClickListener{
                Log.d("SelectedDay", "Current selected day: $selectedDay")

                if(selectedDay != null){
                    dialog.dismiss()
                   // showCustomTimeDialog()
                }else {
                    Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }
    }

    private fun showCustomTimeDialog(selectedDay: String){
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