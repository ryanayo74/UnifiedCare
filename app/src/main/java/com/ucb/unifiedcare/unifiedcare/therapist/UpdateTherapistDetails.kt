package com.ucb.unifiedcare.unifiedcare.therapist

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R

class UpdateTherapistDetails : AppCompatActivity() {

    // Declare UI components and Firestore instance
    private lateinit var specialization: TextView
    private lateinit var addressInput: EditText
    private lateinit var therapyTypeSpinner: Spinner
    private lateinit var autismCheckBox: CheckBox
    private lateinit var adhdCheckBox: CheckBox
    private lateinit var downSyndromeCheckBox: CheckBox
    private lateinit var updateButton: Button
    private lateinit var backLink: TextView
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_therapist_details)

        // Initialize UI components
        specialization = findViewById(R.id.specialization)
        addressInput = findViewById(R.id.addressInput)
        therapyTypeSpinner = findViewById(R.id.therapyType)
        autismCheckBox = findViewById(R.id.autismCheckBox)
        adhdCheckBox = findViewById(R.id.adhdCheckBox)
        downSyndromeCheckBox = findViewById(R.id.downSyndromeCheckBox)
        updateButton = findViewById(R.id.updateButton)
        backLink = findViewById(R.id.backLink)

        // Retrieve data from intent
        val therapistEmail = intent.getStringExtra("therapistEmail")

        // Pre-fill the form with existing data
        if (therapistEmail != null) {
            prefillForm(therapistEmail)
        } else {
            Toast.makeText(this, "No therapist email found.", Toast.LENGTH_SHORT).show()
        }

        // Set up the update button click listener
        updateButton.setOnClickListener {
            if (therapistEmail != null) {
                updateTherapistPreferences(therapistEmail)
            } else {
                Toast.makeText(this, "No therapist email found.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the back link
        backLink.setOnClickListener {
            onBackPressed()
        }
    }

    // Function to pre-fill the form with existing therapist data from Firestore
    private fun prefillForm(therapistEmail: String) {
        firestore.collection("Users")
            .document("therapists")
            .collection("newUserTherapist")
            .document(therapistEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Populate UI components with data from Firestore
                    specialization.text = document.getString("specialization") ?: "N/A"
                    addressInput.setText(document.getString("address") ?: "")
                    val therapyType = document.getString("therapyType") ?: ""
                    val therapyTypes = resources.getStringArray(R.array.therapy_type_array)
                    therapyTypeSpinner.setSelection(therapyTypes.indexOf(therapyType))

                    // Set checkbox states based on data
                    autismCheckBox.isChecked = document.getBoolean("autism") ?: false
                    adhdCheckBox.isChecked = document.getBoolean("adhd") ?: false
                    downSyndromeCheckBox.isChecked = document.getBoolean("downSyndrome") ?: false
                } else {
                    Toast.makeText(this, "No data found for this therapist.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to update therapist details in Firestore
    private fun updateTherapistPreferences(therapistEmail: String) {
        // Retrieve updated data from UI components
        val updatedSpecialization = specialization.text.toString()
        val updatedAddress = addressInput.text.toString()
        val updatedTherapyType = therapyTypeSpinner.selectedItem.toString()

        // Create a map of updated data
        val updatedData = hashMapOf(
            "specialization" to updatedSpecialization,
            "address" to updatedAddress,
            "therapyType" to updatedTherapyType,
            "autism" to autismCheckBox.isChecked,
            "adhd" to adhdCheckBox.isChecked,
            "downSyndrome" to downSyndromeCheckBox.isChecked
        )

        // Update the therapist data in Firestore
        firestore.collection("Users")
            .document("therapists")
            .collection("newUserTherapist")
            .document(therapistEmail)
            .update(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update preferences: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
