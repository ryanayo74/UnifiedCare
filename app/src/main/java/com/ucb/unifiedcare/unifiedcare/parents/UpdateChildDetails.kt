package com.ucb.unifiedcare.unifiedcare.parents

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R

class UpdateChildDetails : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_child_details)

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Retrieve UI elements
        val firstNameField = findViewById<EditText>(R.id.c_frstName)
        val lastNameField = findViewById<EditText>(R.id.c_lastName)
        val specialNeedsSpinner = findViewById<Spinner>(R.id.specialneeds)
        val therapyTypeSpinner = findViewById<Spinner>(R.id.therapytype)
        val ageRangeSpinner = findViewById<Spinner>(R.id.agerange)
        val addressField = findViewById<EditText>(R.id.address)
        val updateButton = findViewById<Button>(R.id.updatebtn)

        // Set up the spinners
        setupSpinners(specialNeedsSpinner, therapyTypeSpinner, ageRangeSpinner)

        // Pre-fill the form with existing data
        prefillForm(
            firstNameField, lastNameField, specialNeedsSpinner, therapyTypeSpinner,
            ageRangeSpinner, addressField
        )

        // Set onClickListener for update button
        updateButton.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val specialNeeds = specialNeedsSpinner.selectedItem.toString()
            val therapyType = therapyTypeSpinner.selectedItem.toString()
            val ageRange = ageRangeSpinner.selectedItem.toString()
            val address = addressField.text.toString().trim()

            if (validateInputs(firstName, lastName, specialNeeds, therapyType, ageRange, address)) {
                updateChildDetails(firstName, lastName, specialNeeds, therapyType, ageRange, address)
            } else {
                Toast.makeText(this, "Please fill all the fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateChildDetails(
        firstName: String, lastName: String, specialNeeds: String, therapyType: String,
        ageRange: String, address: String
    ) {
        val currentUserEmail = auth.currentUser?.email ?: return

        val childDetails = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "specialNeeds" to specialNeeds,
            "therapyType" to therapyType,
            "ageRange" to ageRange,
            "address" to address
        )

        db.collection("Users")
            .document("parents")
            .collection("newUserParent")
            .document(currentUserEmail)
            .update("childDetails", childDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Child details updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupSpinners(specialNeedsSpinner: Spinner, therapyTypeSpinner: Spinner, ageRangeSpinner: Spinner) {
        // Set up the special needs spinner
        val specialNeedsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.special_needs_array,
            android.R.layout.simple_spinner_item
        )
        specialNeedsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        specialNeedsSpinner.adapter = specialNeedsAdapter

        // Set up the therapy type spinner
        val therapyTypeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.therapy_type_array,
            android.R.layout.simple_spinner_item
        )
        therapyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        therapyTypeSpinner.adapter = therapyTypeAdapter

        // Set up the age range spinner
        val ageRangeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_range_array,
            android.R.layout.simple_spinner_item
        )
        ageRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageRangeSpinner.adapter = ageRangeAdapter
    }

    private fun prefillForm(
        firstNameField: EditText, lastNameField: EditText, specialNeedsSpinner: Spinner,
        therapyTypeSpinner: Spinner, ageRangeSpinner: Spinner, addressField: EditText
    ) {
        val currentUserEmail = auth.currentUser?.email ?: return

        db.collection("Users")
            .document("parents")
            .collection("newUserParent")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val childDetails = document.get("childDetails") as? Map<String, Any>

                    childDetails?.let {
                        firstNameField.setText(it["firstName"] as? String ?: "")
                        lastNameField.setText(it["lastName"] as? String ?: "")
                        addressField.setText(it["address"] as? String ?: "")

                        val specialNeeds = it["specialNeeds"] as? String ?: ""
                        val therapyType = it["therapyType"] as? String ?: ""
                        val ageRange = it["ageRange"] as? String ?: ""

                        specialNeedsSpinner.setSelection(
                            resources.getStringArray(R.array.special_needs_array).indexOf(specialNeeds)
                        )
                        therapyTypeSpinner.setSelection(
                            resources.getStringArray(R.array.therapy_type_array).indexOf(therapyType)
                        )
                        ageRangeSpinner.setSelection(
                            resources.getStringArray(R.array.age_range_array).indexOf(ageRange)
                        )
                    }
                } else {
                    Toast.makeText(this, "No data found for this user.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateInputs(
        firstName: String, lastName: String, specialNeeds: String, therapyType: String,
        ageRange: String, address: String
    ): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && specialNeeds.isNotEmpty() &&
                therapyType.isNotEmpty() && ageRange.isNotEmpty() && address.isNotEmpty()
    }
}
