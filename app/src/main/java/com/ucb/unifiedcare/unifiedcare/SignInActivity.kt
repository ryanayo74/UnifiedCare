package com.ucb.unifiedcare.unifiedcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ucb.unifiedcare.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.ucb.unifiedcare.unifiedcare.parents.ForgotPasswordPageActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val db = Firebase.firestore // Firestore DB wala pako nag butang pero naka connect nani sa firestore ang project
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

        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordPageActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, UserTypePageActivity::class.java)
            startActivity(intent)
        }

        // Show or hide password
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        radioButtonRememberMe.setOnClickListener {
            rememberMeChecked = !rememberMeChecked
            radioButtonRememberMe.isChecked = rememberMeChecked
            if (rememberMeChecked) {
                val savedUserName = sharedPreferences.getString("username", "")
                val savedPassword = sharedPreferences.getString("password", "")
                if (!savedUserName.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                    editTextUserName.setText(savedUserName)
                    editTextPassword.setText(savedPassword)
                }
            } else {
                editTextUserName.setText("")
                editTextPassword.setText("")
            }
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

                            //REDIRECT TO PARENT HOMEPAGE
                            //INTENT CODE HERE (MISSING LANG SAH)

                        } else {
                            auth.signOut()
                            Toast.makeText(
                                this,
                                "Please verify your email address.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this, "Incorrect Email or Password.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}