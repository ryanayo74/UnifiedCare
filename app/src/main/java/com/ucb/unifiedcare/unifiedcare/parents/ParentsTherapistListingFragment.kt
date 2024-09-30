package com.ucb.unifiedcare.unifiedcare.parents

import Adapter.TherapistAdapter
import ModelClass.Therapist
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R

class ParentsTherapistListingFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: TherapistAdapter
    private val therapists = mutableListOf<Therapist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_parents_therapist_listing, container, false)

        //initialize firestore
        firestore = FirebaseFirestore.getInstance()

        //Setup the recycler view
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TherapistAdapter(therapists)  { therapist ->
            // Handle facility click
            val intent = Intent(context, TherapistInformationPageActivity::class.java)
            intent.putExtra("therapistName", therapist.fullName)
            intent.putExtra("therapistType", therapist.therapyType)// Pass the facility ID or other details
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // Fetch facility data from Firestore
        fetchTherapists()
        return view
    }
    private fun fetchTherapists() {
        firestore.collection("Users")
            .document("therapists")
            .collection("newUserTherapist")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("fullName") ?: ""
                    val description = document.getString("therapyType") ?: ""
                   // val imageUrl = document.getString("imageUrl") ?: ""
                 //   val rating = 4.5f // Default rating, modify as needed

                    // Create Facility object
                    val therapist = Therapist(name, description)
                    therapists.add(therapist)
                    
                }
                // Notify adapter that data has changed
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching facilities: ", exception)
            }
    }

}