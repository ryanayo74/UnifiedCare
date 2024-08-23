package com.ucb.unifiedcare.unifiedcare.therapist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ucb.unifiedcare.R

class TherapistSignUpPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_sign_up_page)

        val firstName = findViewById<EditText>(R.id.firstName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val email = findViewById<EditText>(R.id.email)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val proceedButton = findViewById<Button>(R.id.proceedButton)

        proceedButton.setOnClickListener {
            // Check if any field is empty
            when {
                firstName.text.toString().isEmpty() -> {
                    firstName.error = "First name is required"
                    firstName.requestFocus()
                }
                lastName.text.toString().isEmpty() -> {
                    lastName.error = "Last name is required"
                    lastName.requestFocus()
                }
                email.text.toString().isEmpty() -> {
                    email.error = "Email is required"
                    email.requestFocus()
                }
                phoneNumber.text.toString().isEmpty() -> {
                    phoneNumber.error = "Phone number is required"
                    phoneNumber.requestFocus()
                }
                password.text.toString().isEmpty() -> {
                    password.error = "Password is required"
                    password.requestFocus()
                }
                confirmPassword.text.toString().isEmpty() -> {
                    confirmPassword.error = "Confirm password is required"
                    confirmPassword.requestFocus()
                }
                password.text.toString() != confirmPassword.text.toString() -> {
                    confirmPassword.error = "Passwords do not match"
                    confirmPassword.requestFocus()
                }
                else -> {
                    // All fields are filled, proceed to the next activity
                    val intent = Intent(this, TherapistNewUserSignUpPage2Activity::class.java).apply {
                        putExtra("firstName", firstName.text.toString())
                        putExtra("lastName", lastName.text.toString())
                        putExtra("email", email.text.toString())
                        putExtra("phoneNumber", phoneNumber.text.toString())
                        putExtra("password", password.text.toString())
                        putExtra("confirmPassword", confirmPassword.text.toString())
                    }
                    startActivity(intent)
                }
            }
        }
    }
}
