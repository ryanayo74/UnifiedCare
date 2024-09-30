package com.ucb.unifiedcare.unifiedcare.parents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ucb.unifiedcare.R

class TherapistInformationPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_information_page)

        //receive intent data
        val name = intent.getStringExtra("therapistName")
        val therapyType = intent.getStringExtra("therapistType")
        val full_name = findViewById<TextView>(R.id.therapist_fullname)
        val type = findViewById<TextView>(R.id.therapy_type)
        full_name.text=name
        type.text = therapyType
    }
}