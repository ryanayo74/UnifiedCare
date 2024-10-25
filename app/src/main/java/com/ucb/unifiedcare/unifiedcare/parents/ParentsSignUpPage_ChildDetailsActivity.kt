package com.ucb.unifiedcare.unifiedcare.parents

import ModelClass.NominatimResult
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ucb.unifiedcare.MapFragment
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.SignInActivity
import com.ucb.unifiedcare.unifiedcare.therapist.NominatimRetrofit
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.api.IMapController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParentsSignUpPage_ChildDetailsActivity : AppCompatActivity() {

    // Firestore and Auth instances
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var webView: WebView
    var isMapVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_sign_up_page_child_details)

        // Retrieve input fields and other views
        val firstNameField = findViewById<EditText>(R.id.c_frstName)
        val lastNameField = findViewById<EditText>(R.id.c_lastName)
        val specialNeedsSpinner = findViewById<Spinner>(R.id.specialneeds)
        val therapyTypeSpinner = findViewById<Spinner>(R.id.therapytype)
        val ageRangeSpinner = findViewById<Spinner>(R.id.agerange)
        val addressField = findViewById<EditText>(R.id.address)
        val registerButton = findViewById<Button>(R.id.registerbtn)

        // Initialize Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //MAP implementation
        webView = findViewById(R.id.webview)
        // Set up the WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true

        // Load the leaflet map HTML file
        webView.loadUrl("file:///android_asset/leafllet_map.html")

        // Hide map initially
        webView.visibility = View.GONE

        // Show map when EditText is clicked
        addressField.setOnClickListener {
            webView.visibility = View.VISIBLE
            isMapVisible = true
        }

        // Handle manual address input
        addressField.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val userAddress = addressField.text.toString().trim()
                if (userAddress.isNotEmpty()) {
                    val encodedAddress = Uri.encode(userAddress)
                    webView.evaluateJavascript("javascript:moveMarkerToAddress('$encodedAddress');", null)
                }
                true
            } else {
                false
            }
        }
        // Add this inside your onCreate method after initializing the WebView
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun updateAddressField(address: String) {
                runOnUiThread {
                    addressField.setText(address)  // Update the EditText with the address
                }
            }
        }, "Android")

        // Add this inside your onCreate method after initializing the WebView
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun closeMap() {
                runOnUiThread {
                    webView.visibility = View.GONE  // Hide the WebView (map)
                    isMapVisible = false  // Update the visibility flag
                }
            }
        }, "Android")
        //END OF MAP IMPLEMENTATION


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

            // Retrieve parent details from the intent
            val parentFirstName = intent.getStringExtra("p_firstName") ?: "DefaultFirstName"
            val parentLastName = intent.getStringExtra("p_lastName") ?: "DefaultLastName"
            val parentPhone = intent.getStringExtra("p_phone") ?: "DefaultPhone"

            // Validate inputs
            if (validateInputs(firstName, lastName, specialNeeds, therapyType, ageRange, address)) {
                val email = intent.getStringExtra("p_email")
                val password = intent.getStringExtra("p_password")
                if (email != null && password != null) {
                    signUpWithEmail(
                        email, password, firstName, lastName, specialNeeds, therapyType, ageRange, address,
                        parentFirstName, parentLastName, parentPhone
                    )
                } else {
                    Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpWithEmail(
        email: String, password: String, firstName: String, lastName: String,
        specialNeeds: String, therapyType: String, ageRange: String, address: String,
        parentFirstName: String, parentLastName: String, parentPhone: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, save the user's data
                    val user = auth.currentUser
                    if (user != null) {
                        // Send email verification
                        user.sendEmailVerification()
                            .addOnCompleteListener { verificationTask ->
                                if (verificationTask.isSuccessful) {
                                    Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show()

                                    // Save to Firestore after sending email verification
                                    saveToFirestore(
                                        firstName, lastName, specialNeeds, therapyType, ageRange, address,
                                        parentFirstName, parentLastName, email, parentPhone, password
                                    )
                                } else {
                                    Toast.makeText(this, "Failed to send verification email: ${verificationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // If sign-in fails, display a message to the user.
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
            "childDetails" to childDetails,
            "createdAt" to FieldValue.serverTimestamp()
        )

        // Concatenate parentFirstName and parentLastName to create a unique document ID
        val documentId = "$parentEmail"


        // Save details to Firestore under the "Users" collection -> "parents" sub-collection
        if (parentFirstName != null && parentLastName != null) {
            db.collection("Users")
                .document("parents") // Name of the parent document
                .collection("newUserParent") // Sub-collection for parent details
                .document(documentId) // Use concatenated parent first name and last name as the document ID
                .set(userDetails) // Use set() to set the data in the document with the specified ID
                .addOnSuccessListener {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                    // Redirect to SignInActivity after successful registration
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish() // Close the current activity so the user can't navigate back to it
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(
                this,
                "Parent first and last name are required to generate document ID.",
                Toast.LENGTH_SHORT
            ).show()
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

