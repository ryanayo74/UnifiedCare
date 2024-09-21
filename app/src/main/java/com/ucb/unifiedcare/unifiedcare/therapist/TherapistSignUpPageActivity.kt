package com.ucb.unifiedcare.unifiedcare.therapist

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ucb.unifiedcare.R

class TherapistSignUpPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_sign_up_page)

        val checkBoxShowPassword: CheckBox = findViewById(R.id.checkBoxShowPassword)
        val checkBoxShowPassword2: CheckBox = findViewById(R.id.checkBoxShowPassword2)
        val firstName = findViewById<EditText>(R.id.firstName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val email = findViewById<EditText>(R.id.email)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val proceedButton = findViewById<Button>(R.id.proceedButton)

        // Show or hide the password
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            password.setSelection(password.text.length)
        }

        // Show or hide the confirm password
        checkBoxShowPassword2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                confirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                confirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            confirmPassword.setSelection(confirmPassword.text.length)
        }

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
                !isValidPhilippineNumber(phoneNumber.text.toString()) -> {
                    phoneNumber.error = "Enter a valid Philippine phone number"
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
                    // All fields are valid, proceed to the next activity
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

    // Function to validate Philippine phone number
    private fun isValidPhilippineNumber(phone: String): Boolean {
        // Allow either format: 09123456789 or +639123456789
        val regex = Regex("^09\\d{9}\$|^\\+639\\d{9}\$")
        return regex.matches(phone)
    }
}
