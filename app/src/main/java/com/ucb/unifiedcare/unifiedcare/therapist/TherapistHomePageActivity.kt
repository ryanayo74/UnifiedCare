package com.ucb.unifiedcare.unifiedcare.therapist

import Adapter.FacilityAdapter
import ModelClass.Facility
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.ProfilePageActivity
import com.ucb.unifiedcare.unifiedcare.parents.ParentsFacilityInformationActivity
import com.ucb.unifiedcare.unifiedcare.parents.TherapistInformationPageActivity

class TherapistHomePageActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: FacilityAdapter
    private val facilities = mutableListOf<Facility>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_home_page)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FacilityAdapter(facilities) { facility ->
            // Handle facility click
            val intent = Intent(this, ParentsFacilityInformationActivity::class.java)
            intent.putExtra("facilityId", facility.name) // Pass the facility ID or other details
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Fetch facility data from Firestore
        fetchFacilities()

        // Profile Image Click Listener
        val imageView: ImageView = findViewById(R.id.profileImage)
        val intent = Intent(this, ProfilePageActivity::class.java)
        imageView.setOnClickListener {
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
                    val address = document.getString("adress")?: ""
                    val email = document.getString("email")?: ""
                    val phoneNumber = document.getString("phoneNumber")?: ""
                    val rating = 4.5f // You can fetch or calculate this based on your own logic

                    // Create Facility object
                    val facility = Facility(name, description, imageUrl, email, phoneNumber, address, rating,false)
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

