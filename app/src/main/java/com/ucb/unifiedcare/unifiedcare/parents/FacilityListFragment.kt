package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.FacilityAdapter
import ModelClass.Facility
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.RetrofitInstance
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class FacilityListFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: FacilityAdapter
    private val facilities = mutableListOf<Facility>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_facility_list, container, false)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Adapter with click listener
        adapter = FacilityAdapter(facilities) { document ->
            // Handle facility click
            val intent = Intent(context, ParentsFacilityInformationActivity::class.java)
            intent.putExtra("facilityDesc", document.description)
            intent.putExtra("phone_number", document.phoneNumber)
            intent.putExtra("email", document.email)
            intent.putExtra("address", document.address)
            intent.putExtra("id", document.docId)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        // Fetch facility data from Firestore
        fetchFacilitiesFromAPI()
        return view
    }

    private fun fetchFacilitiesFromAPI() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getFacilities("childsdetails")
                if (response.isSuccessful) {
                    response.body()?.let { apiFacilities ->
                        for (facility in apiFacilities) {
                            // Use the facility.clinic_id to fetch the corresponding image
                            fetchProfilePic(facility.clinic_id ) { docId, imageUrl, address, phoneNumber, email ->
                                if (imageUrl != null) {
                                    facility.imageUrl = imageUrl
                                }
                                if (address != null) {
                                    facility.address = address
                                }
                                if (phoneNumber != null) {
                                    facility.phoneNumber = phoneNumber
                                }
                                if (email != null) {
                                    facility.email = email
                                }
                                if (docId != null) {
                                    facility.docId = docId
                                }
                                val intent = Intent(context, ParentsFacilityInformationActivity::class.java)
                                intent.putExtra("address", facility.address)
                                intent.putExtra("email", facility.email)
                                intent.putExtra("phone_number", facility.phoneNumber)

                                facilities.add(facility)

                                // Notify the adapter when all data is ready
                                if (facilities.size == apiFacilities.size) {
                                    adapter.notifyDataSetChanged()  // Update RecyclerView
                                }
                            }
                        }
                    }
                } else {
                    Log.e("API Error", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Network Error", "Failed: ${e.message}")
            }
        }
    }

    private fun fetchProfilePic(clinicId: String,onResult: (String?, String?, String?, String?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Query the userFacility collection where the facility_id matches the clinicId
        val userFacilityRef = db.collection("Users")
            .document("facility")
            .collection("userFacility")
            .whereEqualTo("facility_id", clinicId)  // Query based on facility_id field

        userFacilityRef.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                // Assuming there's only one match for the facility_id
                val documentSnapshot = querySnapshot.documents[0]
                val docId = documentSnapshot.id
                Log.d("docId", docId)
                val imageUrl = documentSnapshot.getString("image")
                val address = documentSnapshot.getString("address")
                val phoneNumber = documentSnapshot.getString("phoneNumber")
                val email = documentSnapshot.getString("email")
                onResult(docId, imageUrl, address, phoneNumber, email)
            } else {
                onResult(null, null, null, null, null) // No document found with matching facility_id
            }
        }.addOnFailureListener {
            onResult(null, null, null, null, null)
        }
    }
}