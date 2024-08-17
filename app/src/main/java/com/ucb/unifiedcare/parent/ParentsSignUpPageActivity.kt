package com.ucb.unifiedcare.parent

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ucb.unifiedcare.R

class ParentsSignUpPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parents_sign_up_page)

        val editTextFirstName: EditText = findViewById(R.id.p_firstName)
        val editTextLastName: EditText = findViewById(R.id.p_lastName)
        val editTextEmail: EditText = findViewById(R.id.p_email)
        val editTextPhone: EditText = findViewById(R.id.p_phone)
        val editTextPassword: EditText = findViewById(R.id.p_passwrord)
        val editTextConfirmPassword: EditText = findViewById(R.id.p_confirmpass)
        val checkBoxShowPassword: CheckBox = findViewById(R.id.checkBoxShowPassword)
        val checkBoxShowPassword2: CheckBox = findViewById(R.id.checkBoxShowPassword2)
        val proceedBtn: Button = findViewById(R.id.proceedBtn)

        // Show or hide the password
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            editTextPassword.setSelection(editTextPassword.text.length)
        }

        // Show or hide the confirm password
        checkBoxShowPassword2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                editTextConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            editTextConfirmPassword.setSelection(editTextConfirmPassword.text.length)
        }

        // Proceed sa next page nga with validation
        proceedBtn.setOnClickListener {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            // Validate first name and last name
            if (firstName.isEmpty()) {
                editTextFirstName.error = "First name is required"
                editTextFirstName.requestFocus()
                return@setOnClickListener
            }

            if (lastName.isEmpty()) {
                editTextLastName.error = "Last name is required"
                editTextLastName.requestFocus()
                return@setOnClickListener
            }

            // Validate email
            if (email.isEmpty()) {
                editTextEmail.error = "Email is required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = "Please enter a valid email"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate phone number
            if (phone.isEmpty()) {
                editTextPhone.error = "Phone number is required"
                editTextPhone.requestFocus()
                return@setOnClickListener
            }

            if (!phone.matches(Regex("^\\+63\\d{10}\$"))) {
                editTextPhone.error = "Please enter a valid Philippine phone number starting with +63"
                editTextPhone.requestFocus()
                return@setOnClickListener
            }

            // Validate password sample lang sah ang password para dali rah i test kapoy type
            if (password.isEmpty()) {
                editTextPassword.error = "Password is required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                editTextPassword.error = "Password must be at least 6 characters"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            // Validate confirm password sample lang sah ang password para dali rah i test kapoy type
            if (confirmPassword.isEmpty()) {
                editTextConfirmPassword.error = "Please confirm your password"
                editTextConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                editTextConfirmPassword.error = "Passwords do not match"
                editTextConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // If all validations pass, proceed to the next page with details sa parents
            val intent = Intent(this, ParentsSignUpPage_ChildDetailsActivity::class.java)
            intent.putExtra("p_firstName", firstName)
            intent.putExtra("p_lastName", lastName)
            intent.putExtra("p_email", email)
            intent.putExtra("p_phone", phone)
            intent.putExtra("p_password", password)
            startActivity(intent)
        }
    }
}
