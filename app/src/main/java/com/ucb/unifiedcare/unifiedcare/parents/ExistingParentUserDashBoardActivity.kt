package com.ucb.unifiedcare.unifiedcare.parents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.ucb.unifiedcare.R

class ExistingParentUserDashBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_parent_user_dash_board)

        val menuBtn: ImageView = findViewById(R.id.menu_icon)
    }
}