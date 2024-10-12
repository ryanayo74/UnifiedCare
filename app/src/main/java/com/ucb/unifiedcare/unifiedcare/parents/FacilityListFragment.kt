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
import kotlinx.coroutines.launch
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
        adapter = FacilityAdapter(facilities) { facility ->
            // Handle facility click
            val intent = Intent(context, ParentsFacilityInformationActivity::class.java)
            intent.putExtra("facilityDesc", facility.description)
            intent.putExtra("phone_number", facility.phoneNumber)
            intent.putExtra("email", facility.email)
            intent.putExtra("address", facility.address)// Pass the facility ID or other details
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // Fetch facility data from Firestore
        fetchFacilities()
        return view
    }
    private fun fetchFacilities(){
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getFacilities("childsdetails")
                if (response.isSuccessful) {
                    response.body()?.let {
                        facilities.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }  else {
                    Log.e("API Error", "Error: ${response.message()}")
                }
            }catch(e: Exception){
                Log.e("Network Error", "Failed: ${e.message}")
            }
        }
    }
//    private fun fetchFacilities() {
//        firestore.collection("Users")
//            .document("facility")
//            .collection("userFacility")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val name = document.getString("name") ?: ""
//                    val description = document.getString("description") ?: ""
//                    val imageUrl = document.getString("image") ?: ""
//                    val address = document.getString("address")?: ""
//                    val email = document.getString("email")?: ""
//                    val phoneNumber = document.getString("phoneNumber")?: ""
//
//                    val rating = 4.5f // Default rating, modify as needed
//
//                    // Create Facility object
//                    val facility = Facility(name, description, imageUrl, email, phoneNumber, address, rating,false)
//                    facilities.add(facility)
//                }
//                // Notify adapter that data has changed
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.e("FirestoreError", "Error fetching facilities: ", exception)
//            }
//    }
}