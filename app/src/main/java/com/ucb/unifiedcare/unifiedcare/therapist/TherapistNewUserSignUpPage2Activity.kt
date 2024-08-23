package com.ucb.unifiedcare.unifiedcare.therapist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.SignInActivity

class TherapistNewUserSignUpPage2Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_new_user_sign_up_page2)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve the data passed from the first activity
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val password = intent.getStringExtra("password")
        val confirmPassword = intent.getStringExtra("confirmPassword")

        // Retrieve CheckBox states for specializations
        val autismCheckBox = findViewById<CheckBox>(R.id.autismCheckBox)
        val adhdCheckBox = findViewById<CheckBox>(R.id.adhdCheckBox)
        val downSyndromeCheckBox = findViewById<CheckBox>(R.id.downSyndromeCheckBox)

        // Collect selected specializations
        val specializations = mutableListOf<String>()
        if (autismCheckBox.isChecked) specializations.add("Autism")
        if (adhdCheckBox.isChecked) specializations.add("ADHD")
        if (downSyndromeCheckBox.isChecked) specializations.add("Down Syndrome")

        // Get the entered address
        val address = findViewById<EditText>(R.id.addressInput).text.toString()

        // Get selected therapy type
        val therapyType = findViewById<Spinner>(R.id.therapyTypeSpinner).selectedItem.toString()

        // Combine the details into a HashMap
        val therapistData = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "specializations" to specializations,
            "therapyType" to therapyType,
            "address" to address
        )

        // Concatenate firstName and lastName to create a unique document ID
        val documentId = "${firstName}_${lastName}"

        // Set up the Save button listener
        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            if (password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save details to Firestore under the "Users" collection -> "therapists" sub-collection
                            firestore.collection("Users")
                                .document("therapists") // Name of the parent document
                                .collection("newUserTherapist") // Sub-collection for therapist details
                                .document(documentId) // Use concatenated first name and last name as the document ID
                                .set(therapistData) // Use set() to set the data in the document with the specified ID
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                                    // Redirect to SignInActivity after successful registration
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish() // Close the current activity so the user can't navigate back to it
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
