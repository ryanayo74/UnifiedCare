package com.ucb.unifiedcare.unifiedcare.therapist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ucb.unifiedcare.R


class TherapistConfirmationFacilityPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_therapist_confirmation_facility_page)

        val yesButton: Button = findViewById(R.id.yesButton)
        val noButton: Button = findViewById(R.id.noButton)

        yesButton.setOnClickListener {
            val intent = Intent(this, TherapistConfirmationFacilityPageEmailActivity::class.java)
            startActivity(intent)
        }

        noButton.setOnClickListener {
            val intent = Intent(this, TherapistSignUpPageActivity::class.java)
            startActivity(intent)
        }

    }
}