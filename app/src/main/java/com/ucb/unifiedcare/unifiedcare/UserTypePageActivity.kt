package com.ucb.unifiedcare.unifiedcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.parents.ParentsSignUpPageActivity
import com.ucb.unifiedcare.unifiedcare.therapist.TherapistConfirmationFacilityPageActivity

class UserTypePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type_page)

        val therapistRegistration: Button = findViewById(R.id.therapistsbtn)
        val parentsRegistration: Button = findViewById(R.id.parentbtn)

        parentsRegistration.setOnClickListener {
            val intent = Intent(this, ParentsSignUpPageActivity::class.java)
            startActivity(intent)
        }

        therapistRegistration.setOnClickListener {
            val intent = Intent(this, TherapistConfirmationFacilityPageActivity::class.java)
            startActivity(intent)
        }
    }
}