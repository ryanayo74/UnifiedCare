package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.FacilityAdapter
import ModelClass.Facility
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.ProfilePageActivity

class ParentsFacilityListActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: FacilityAdapter
    private val facilities = mutableListOf<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_facility_list)

        val facilityBtn: Button = findViewById(R.id.facilitylistbtn)
        val therapistBtn: Button = findViewById(R.id.therapistslistbtn)
        val profile = findViewById<ImageView>(R.id.profile_icon)
        val intent = Intent(this, ProfilePageActivity::class.java)

        profile.setOnClickListener{
            startActivity(intent)
        }

        //initially load the facility list fragment
        loadFragment(FacilityListFragment())

        //set listeners for the buttons
        facilityBtn.setOnClickListener{
            loadFragment(FacilityListFragment())
        }

        therapistBtn.setOnClickListener{
            loadFragment(ParentsTherapistListingFragment())
        }


    }
    // Method to load the specified fragment into the container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.facility_fragment_container, fragment)
            .commit()
    }
}
