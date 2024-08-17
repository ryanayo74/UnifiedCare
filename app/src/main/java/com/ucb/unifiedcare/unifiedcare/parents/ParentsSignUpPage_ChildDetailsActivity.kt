package com.ucb.unifiedcare.unifiedcare.parents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ucb.unifiedcare.R

class ParentsSignUpPage_ChildDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_sign_up_page_child_details)

        val userDetails = intent.getStringArrayExtra("USER_DETAILS")

        // Parents details na gikan sa Parents Page
        if (userDetails != null) {
            val firstName = userDetails[0]
            val lastName = userDetails[1]
            val email = userDetails[2]
            val phone = userDetails[3]
            val password = userDetails[4]


        }
    }
}
