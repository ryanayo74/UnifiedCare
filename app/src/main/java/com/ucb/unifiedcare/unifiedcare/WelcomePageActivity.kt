package com.ucb.unifiedcare.parent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.SignInActivity
import com.ucb.unifiedcare.unifiedcare.UserTypePageActivity

class WelcomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        val buttonExplore: Button = findViewById(R.id.explorebtn)

        val intent = Intent(this, SignInActivity::class.java)

        buttonExplore.setOnClickListener {
            startActivity(intent)
        }
    }
}