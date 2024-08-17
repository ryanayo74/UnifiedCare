package com.ucb.unifiedcare.unifiedcare.parents

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R

class ParentsSignUpPage_ChildDetailsActivity : AppCompatActivity() {

    // Firestore instance
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_sign_up_page_child_details)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Retrieve parent details from the Intent
        val parentFirstName = intent.getStringExtra("p_firstName")
        val parentLastName = intent.getStringExtra("p_lastName")
        val parentEmail = intent.getStringExtra("p_email")
        val parentPhone = intent.getStringExtra("p_phone")
        val parentPassword = intent.getStringExtra("p_password")

        // Retrieve input fields
        val firstNameField = findViewById<EditText>(R.id.c_frstName)
        val lastNameField = findViewById<EditText>(R.id.c_lastName)
        val specialNeedsSpinner = findViewById<Spinner>(R.id.specialneeds)
        val therapyTypeSpinner = findViewById<Spinner>(R.id.therapytype)
        val ageRangeSpinner = findViewById<Spinner>(R.id.agerange)
        val addressField = findViewById<EditText>(R.id.address)
        val registerButton = findViewById<Button>(R.id.registerbtn)

        // Set up the special needs spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.special_needs_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        specialNeedsSpinner.adapter = adapter

        // Set an onClickListener on the register button
        registerButton.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val specialNeeds = specialNeedsSpinner.selectedItem.toString()
            val therapyType = therapyTypeSpinner.selectedItem.toString()
            val ageRange = ageRangeSpinner.selectedItem.toString()
            val address = addressField.text.toString().trim()

            // Validate inputs
            if (validateInputs(firstName, lastName, specialNeeds, therapyType, ageRange, address)) {
                // Save details to Firestore
                saveToFirestore(
                    firstName, lastName, specialNeeds, therapyType, ageRange, address,
                    parentFirstName, parentLastName, parentEmail, parentPhone, parentPassword
                )
            } else {
                // Show error message
                Toast.makeText(this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        // Optionally populate fields if needed
    }

    private fun saveToFirestore(
        firstName: String, lastName: String, specialNeeds: String, therapyType: String,
        ageRange: String, address: String,
        parentFirstName: String?, parentLastName: String?, parentEmail: String?,
        parentPhone: String?, parentPassword: String?
    ) {
        // Create a map of the details to be saved
        val childDetails = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "specialNeeds" to specialNeeds,
            "therapyType" to therapyType,
            "ageRange" to ageRange,
            "address" to address
        )

        val parentDetails = hashMapOf(
            "firstName" to parentFirstName,
            "lastName" to parentLastName,
            "email" to parentEmail,
            "phone" to parentPhone,
            "password" to parentPassword
        )

        val userDetails = hashMapOf(
            "parentDetails" to parentDetails,
            "childDetails" to childDetails
        )

        // Save details to Firestore under the "Users" collection -> "parents" sub-collection
        parentFirstName?.let {
            db.collection("Users")
                .document("parents") // Name of the parent document
                .collection("parent") // Sub-collection for parent details
                .document(it) // Use parent's first name as the document ID
                .set(userDetails) // Use set() to set the data in the document with the specified ID
                .addOnSuccessListener {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "Parent first name is required to generate document ID.", Toast.LENGTH_SHORT).show()
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
