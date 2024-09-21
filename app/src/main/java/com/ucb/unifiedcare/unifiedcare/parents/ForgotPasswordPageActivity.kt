package com.ucb.unifiedcare.unifiedcare.parents

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.SignInActivity

class ForgotPasswordPageActivity : AppCompatActivity() {

    private lateinit var forgotPassEmail: EditText
    private lateinit var resetPasswordBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_page) // Ensure you have the right XML layout name

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize UI elements
        forgotPassEmail = findViewById(R.id.forgotPassEmail)
        resetPasswordBtn = findViewById(R.id.resetpasswordBtn)

        // Set click listener on the reset button
        resetPasswordBtn.setOnClickListener {
            val email = forgotPassEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    this@ForgotPasswordPageActivity,
                    "Please enter your registered email",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Send password reset email
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotPasswordPageActivity,
                            "Password reset email sent",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirect to SignInActivity after successful reset email
                        val intent = Intent(this@ForgotPasswordPageActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: Finish the current activity to prevent going back
                    } else {
                        Toast.makeText(
                            this@ForgotPasswordPageActivity,
                            "Failed to send reset email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
