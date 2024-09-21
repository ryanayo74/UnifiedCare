package com.ucb.unifiedcare.unifiedcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ucb.unifiedcare.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ucb.unifiedcare.unifiedcare.parents.ExistingParentUserDashBoardActivity
import com.ucb.unifiedcare.unifiedcare.parents.ForgotPasswordPageActivity
import com.ucb.unifiedcare.unifiedcare.parents.ParentsFacilityListActivity
import com.ucb.unifiedcare.unifiedcare.therapist.TherapistApplyHomePageActivity
import com.ucb.unifiedcare.unifiedcare.therapist.TherapistHomePageActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore
    private var rememberMeChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val buttonSignIn: Button = findViewById(R.id.signInBtn)
        val editTextUserName: EditText = findViewById(R.id.email)
        val editTextPassword: EditText = findViewById(R.id.password)
        val radioButtonRememberMe: RadioButton = findViewById(R.id.remember_me_radio_button)
        val forgotPassword: TextView = findViewById(R.id.forgotPassPassword)
        val register: TextView = findViewById(R.id.signUp)
        val checkBoxShowPassword: CheckBox = findViewById(R.id.checkBoxShowPassword)

        // Retrieve saved login details if "Remember Me" is checked
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            val savedUserName = sharedPreferences.getString("username", "")
            val savedPassword = sharedPreferences.getString("password", "")
            if (!savedUserName.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                editTextUserName.setText(savedUserName)
                editTextPassword.setText(savedPassword)
                radioButtonRememberMe.isChecked = true
                rememberMeChecked = true
            }
        }

        // Password visibility toggle
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // Forgot password click listener
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordPageActivity::class.java)
            startActivity(intent)
        }

        // Register button listener
        register.setOnClickListener {
            val intent = Intent(this, UserTypePageActivity::class.java)
            startActivity(intent)
        }

        buttonSignIn.setOnClickListener {
            val inputUserName: String = editTextUserName.text.toString().trim()
            val inputPassword: String = editTextPassword.text.toString().trim()

            if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please input email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(inputUserName, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // Save login details if "Remember Me" is checked
                            if (radioButtonRememberMe.isChecked) {
                                with(sharedPreferences.edit()) {
                                    putString("username", inputUserName)
                                    putString("password", inputPassword)
                                    putBoolean("rememberMe", true)
                                    apply()
                                }
                            } else {
                                with(sharedPreferences.edit()) {
                                    remove("username")
                                    remove("password")
                                    putBoolean("rememberMe", false)
                                    apply()
                                }
                            }
                            // Check the user type and redirect
                            checkUserTypeAndRedirect(user.email)
                        } else {
                            auth.signOut()
                            Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Incorrect Email or Password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun checkUserTypeAndRedirect(email: String?) {
        if (email == null) return

        db.collection("Users").document("parents").collection("newUserParent")
            .whereEqualTo("parentDetails.email", email) // Use dot notation for nested fields
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Redirect to new parent homepage
                    val intent = Intent(this, ParentsFacilityListActivity::class.java)
                    startActivity(intent)
                } else {
                    // Check if the user is an existing parent
                    db.collection("Users").document("parents").collection("existingUserParent")
                        .whereEqualTo(
                            "parentDetails.email",
                            email
                        ) // Use dot notation for nested fields
                        .get()
                        .addOnSuccessListener { existingDocs ->
                            if (!existingDocs.isEmpty) {
                                // Redirect to existing parent homepage
                                val intent = Intent(this, ExistingParentUserDashBoardActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Check for therapist type
                                checkTherapistType(email)
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
            }
    }
        private fun checkTherapistType(email: String) {
        // Check if the user is a new therapist
        db.collection("Users").document("therapists").collection("newUserTherapist")
            .whereEqualTo("email", email) // Query the email inside the therapistDetails map
            .get()
            .addOnSuccessListener { newTherapistDocs ->
                if (!newTherapistDocs.isEmpty) {
                    // Redirect to new therapist homepage
                    val intent = Intent(this, TherapistHomePageActivity::class.java)
                    startActivity(intent)
                } else {
                    // Check if the user is an existing therapist
                    db.collection("Users").document("therapists").collection("existingUserTherapist")
                        .whereEqualTo("email", email) // Query the email inside the therapistDetails map
                        .get()
                        .addOnSuccessListener { existingTherapistDocs ->
                            if (!existingTherapistDocs.isEmpty) {
                                // Redirect to existing therapist homepage
                                val intent = Intent(this, TherapistApplyHomePageActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "No user type found", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to retrieve therapist data.", Toast.LENGTH_SHORT).show()
            }
    }
}

