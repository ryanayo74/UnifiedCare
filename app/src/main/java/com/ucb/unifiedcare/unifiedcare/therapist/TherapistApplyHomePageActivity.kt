package com.ucb.unifiedcare.unifiedcare.therapist

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.ucb.unifiedcare.R

class TherapistApplyHomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_therapist_apply_home_page)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val menuIcon: ImageView = findViewById(R.id.menuIcon)
        val navView: NavigationView = findViewById(R.id.nav_view)

        menuIcon.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Handle navigation menu item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    // Handle the Home action
                }
                R.id.action_messages -> {
                    // Handle the Messages action
                }
                R.id.action_appointment -> {
                    // Handle the Appointment Approval action
                }
                R.id.action_switch_facility -> {
                    // Handle the Appointment Schedule action
                }
                R.id.action_support_group -> {
                    // Handle the Support Group action
                }
                R.id.logout -> {
                    // Handle the Logout action
                }
            }
            // Close drawer after a selection
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
