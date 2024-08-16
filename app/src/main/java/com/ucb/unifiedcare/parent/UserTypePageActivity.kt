package com.ucb.unifiedcare.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ucb.unifiedcare.R

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
    }
}