package com.ucb.unifiedcare.unifiedcare.parents

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R
import Adapter.FacilityAdapter
import ModelClass.Facility
import android.content.Intent
import android.widget.ImageView
import com.ucb.unifiedcare.unifiedcare.ProfilePageActivity

class ParentsFacilityListActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: FacilityAdapter
    private val facilities = mutableListOf<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_facility_list)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FacilityAdapter(facilities)
        recyclerView.adapter = adapter

        // Fetch facility data from Firestore
        fetchFacilities()

       // val notif: ImageView = findViewById(R.id.notif)
        val profile: ImageView = findViewById(R.id.profile_icon)
        val intent = Intent (this, ProfilePageActivity::class.java)
     //   val intent_notif = Intent (this, ProfilePageActivity::class.java)

        profile.setOnClickListener{
            startActivity(intent)
        }
    }

    private fun fetchFacilities() {
        firestore.collection("Users")
            .document("facility")
            .collection("userFacility")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    val imageUrl = document.getString("image") ?: ""
                    val rating = 4.5f // Default rating, modify as needed

                    // Create Facility object
                    val facility = Facility(name, description, imageUrl, rating, false)
                    facilities.add(facility)
                }
                // Notify adapter that data has changed
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching facilities: ", exception)
            }
    }
}
