package com.ucb.unifiedcare.unifiedcare.therapist

import Adapter.FacilityAdapter
import ModelClass.Facility
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.ProfilePageActivity


class TherapistHomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.ucb.unifiedcare.R.layout.activity_therapist_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.ucb.unifiedcare.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(com.ucb.unifiedcare.R.id.recyclerView)

        val facilities = listOf(
            Facility(
                "Facility 1",
                "Facility Example 1.",
                com.ucb.unifiedcare.R.drawable.logo,
                4.5f,
                false
            ),
            Facility(
                "Facility 2",
                "Facility Example 1",
                com.ucb.unifiedcare.R.drawable.logo,
                5.0f,
                true
            ),
            Facility(
                "Facility 3",
                "Facility Example 1",
                com.ucb.unifiedcare.R.drawable.logo,
                4.0f,
                false
            ),
            Facility(
                "Facility 4",
                "Facility Example 1",
                com.ucb.unifiedcare.R.drawable.logo,
                4.5f,
                false
            ),
            Facility(
                "Facility 5",
                "Facility Example 1",
                com.ucb.unifiedcare.R.drawable.logo,
                3.5f,
                false
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = FacilityAdapter(facilities)
        recyclerView.adapter = adapter


        val imageView: ImageView = findViewById(R.id.profileImage)

        val intent = Intent(this, ProfilePageActivity::class.java)

            imageView.setOnClickListener {
                startActivity(intent)
            }
    }
}