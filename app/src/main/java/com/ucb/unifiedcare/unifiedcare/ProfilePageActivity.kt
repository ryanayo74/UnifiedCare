package com.ucb.unifiedcare.unifiedcare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.unifiedcare.therapist.TherapistHomePageActivity

class ProfilePageActivity : AppCompatActivity() {
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var profileImageView: ImageView
    private lateinit var selectImageView: ImageView
    private lateinit var  signOutTextView: TextView
    private lateinit var backArrow : TextView
    private lateinit var aboutApp:  TextView
    private lateinit var termsandcond: TextView
    private lateinit var privacy: TextView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        // Initialize the TextView and ImageView
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        profileImageView = findViewById(R.id.profile)
        selectImageView = findViewById(R.id.circ_design2)
        signOutTextView = findViewById(R.id.topSignOut)
        backArrow = findViewById(R.id.back_arrow)
        aboutApp = findViewById(R.id.AboutApp)
        termsandcond = findViewById(R.id.termAndCond)
        privacy = findViewById(R.id.privacyPolicy)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val intent = Intent(this, SignInActivity::class.java)
        signOutTextView.setOnClickListener {
            startActivity(intent)
        }
        val intent2 = Intent(this, TherapistHomePageActivity::class.java)
        backArrow.setOnClickListener{
            startActivity(intent2)
        }
        val intent3 = Intent(this, UnifiedCareAboutAppActivity::class.java)
        aboutApp.setOnClickListener{
            startActivity(intent3)
        }
        val intent4 = Intent(this, UnifiedCareTermsandConditionActivity::class.java)
        termsandcond.setOnClickListener{
            startActivity(intent4)
        }
        val intent5 = Intent(this, UnifiedCarePrivacyPolicyActivity::class.java)
        privacy.setOnClickListener{
            startActivity(intent5)
        }
        // to open the image picker
        selectImageView.setOnClickListener {
            openImagePicker()
        }
        // Fetch and display the username and profile image
        fetchAndDisplayUserProfile()

        findViewById<TextView>(R.id.changeUserName).setOnClickListener {
            showChangeNameDialog()
        }
        findViewById<TextView>(R.id.changePassword).setOnClickListener {
            showChangePasswordDialog()
        }
    }

    private fun showChangeNameDialog() {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.changeusername_dialog, null)
        val firstNameEditText = dialogView.findViewById<EditText>(R.id.editFirstName)
        val lastNameEditText = dialogView.findViewById<EditText>(R.id.editLastName)

        // Create the AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Change Username")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newFirstName = firstNameEditText.text.toString().trim()
                val newLastName = lastNameEditText.text.toString().trim()
                updateNameInFirestore(newFirstName, newLastName)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateNameInFirestore(firstName: String, lastName: String) {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUserEmail = auth.currentUser?.email ?: return

        // Parent collection reference
        val parentCollectionRef = firestore.collection("Users")
            .document("parents")
            .collection("newUserParent")

        // Therapist collection reference
        val therapistCollectionRef = firestore.collection("Users")
            .document("therapists")
            .collection("newUserTherapist")

        //check if the user is a parent
        parentCollectionRef.whereEqualTo("parentDetails.email", currentUserEmail).get()
            .addOnSuccessListener { parentDocuments ->
                if (!parentDocuments.isEmpty) {
                    // if user is a parent, update the name inside parentDetails
                    val document = parentDocuments.documents[0]
                    val userDocumentRef = document.reference

                    val updates = hashMapOf(
                        "parentDetails.firstName" to firstName,
                        "parentDetails.lastName" to lastName
                    )

                    userDocumentRef.update(updates as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Parent name updated successfully")
                            fetchAndDisplayUserProfile()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Failed to update parent name: ${e.message}")
                        }
                } else {
                    // If not found in parents, try to find in therapists
                    therapistCollectionRef.whereEqualTo("email", currentUserEmail).get()
                        .addOnSuccessListener { therapistDocuments ->
                            if (!therapistDocuments.isEmpty) {
                                //if user is a therapist, update the name at the root level
                                val document = therapistDocuments.documents[0]
                                val userDocumentRef = document.reference

                                val updates = hashMapOf(
                                    "firstName" to firstName,
                                    "lastName" to lastName
                                )

                                userDocumentRef.update(updates as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Therapist name updated successfully")
                                        fetchAndDisplayUserProfile()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore Error", "Failed to update therapist name: ${e.message}")
                                    }
                            } else {
                                Log.d("Firestore Error", "No document found for email")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore Error", "Error finding document: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Error", "Error finding parent document: ${exception.message}")
            }
    }

    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.changepassword_dialog, null)
        val oldPasswordEditText = dialogView.findViewById<EditText>(R.id.oldPassword)
        val newPasswordEditText = dialogView.findViewById<EditText>(R.id.newPassword)

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change") { _, _ ->
                val oldPassword = oldPasswordEditText.text.toString().trim()
                val newPassword = newPasswordEditText.text.toString().trim()
                changeUserPassword(oldPassword, newPassword)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun changeUserPassword(oldPassword: String, newPassword: String) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // Re-authenticate the user with their old password
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Re-authentication successful, proceed to update the password in FirebaseAuth
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    Log.d("PasswordChange", "Password updated successfully")

                                    updatePasswordInFirestore(user.email!!, newPassword)

                                    // Notify the user
                                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.e("PasswordChange", "Failed to update password: ${passwordUpdateTask.exception?.message}")
                                    Toast.makeText(this, "Failed to change password: ${passwordUpdateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // Re-authentication failed
                        Log.e("PasswordChange", "Re-authentication failed: ${task.exception?.message}")
                        Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Log.e("PasswordChange", "No user is currently logged in")
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePasswordInFirestore(email: String, newPassword: String) {
        val firestore = FirebaseFirestore.getInstance()

        // Parent collection reference
        val parentCollectionRef = firestore.collection("Users")
            .document("parents")
            .collection("newUserParent")

        // Search for the parent document using the email
        parentCollectionRef.whereEqualTo("parentDetails.email", email).get()
            .addOnSuccessListener { parentDocuments ->
                if (!parentDocuments.isEmpty) {
                    val document = parentDocuments.documents[0]
                    val parentDocRef = document.reference

                    // Update the password field inside parentDetails
                    val updates = hashMapOf<String, Any>(
                        "parentDetails.password" to newPassword
                    )
                    parentDocRef.update(updates)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Password updated in Firestore for parent")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Failed to update password for parent: ${e.message}")
                        }
                } else {
                    Log.e("Firestore", "No parent document found for the given email")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Error", "Error finding parent document: ${e.message}")
            }
    }

    // Open image picker for profile
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Fetch user profile data including the image and display
    private fun fetchAndDisplayUserProfile() {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            // Check both parent and therapist collections
            val parentCollectionRef = firestore.collection("Users")
                .document("parents")
                .collection("newUserParent")

            val therapistCollectionRef = firestore.collection("Users")
                .document("therapists")
                .collection("newUserTherapist")

            // check if the user is a parent
            parentCollectionRef
                .whereEqualTo("parentDetails.email", currentUserEmail)
                .get()
                .addOnSuccessListener { parentDocuments ->
                    if (!parentDocuments.isEmpty) {
                        val document = parentDocuments.documents[0]
                        val parentDetails = document.get("parentDetails") as Map<String, Any>?

                        if (parentDetails != null) {
                            val parentFirstName = parentDetails["firstName"] as? String
                            val parentLastName = parentDetails["lastName"] as? String
                            val email = parentDetails["email"] as? String
                            val imageUrl = parentDetails["imageUrl"] as? String
                            val fullName = "$parentFirstName $parentLastName"

                            // Set text for username and email
                            userName.text = fullName
                            userEmail.text = email

                            // Load the profile image using Glide
                            if (imageUrl != null && imageUrl.isNotEmpty()) {
                                Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.profile)
                                    .into(profileImageView)
                            }
                    } else {
                            Log.d("ProfilePageActivity", "Parent Details not found")
                        }
                    } else {
                        // If not found in parent, check therapist collection
                        therapistCollectionRef.whereEqualTo("email", currentUserEmail)
                            .get()
                            .addOnSuccessListener { therapistDocuments ->
                                if (!therapistDocuments.isEmpty) {
                                    val therapistDoc = therapistDocuments.documents[0]
                                    val therapistFirstName = therapistDoc.getString("firstName")
                                    val therapistLastName = therapistDoc.getString("lastName")
                                    val email = therapistDoc.getString("email")
                                    val imageUrl = therapistDoc.getString("imageUrl")
                                    val fullName = "$therapistFirstName $therapistLastName"

                                    // Set text for username and email
                                    userName.text = fullName
                                    userEmail.text = email

                                    // Load the profile image using Glide
                                    if (imageUrl != null && imageUrl.isNotEmpty()) {
                                        Glide.with(this)
                                            .load(imageUrl)
                                            .placeholder(R.drawable.profile)
                                            .into(profileImageView)
                                    }
                                } else {
                                    Log.d("ProfilePageActivity", "No document found in both collections")
                                    userName.text = "No user data found"
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("ProfilePageActivity", "Error fetching user data from therapists", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfilePageActivity", "Error fetching user data from parents", exception)
                }
        } else {
            userName.text = "User not logged in"
        }
    }

    // Handle image selection and uploading
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            uploadImageAndSaveUrl(imageUri)
        }
    }

    // Upload image to Firebase Storage and update Firestore with the new image URL
    private fun uploadImageAndSaveUrl(imageUri: Uri) {
        val auth = FirebaseAuth.getInstance()
        val currentUserEmail = auth.currentUser?.email ?: return

        val storageRef = FirebaseStorage.getInstance().reference.child("user_images/${currentUserEmail}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveImageUrlToFirestore(currentUserEmail, imageUrl)
                    // Set the uploaded image immediately in the ImageView
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.profile) // this is optional default placeholder
                        .into(profileImageView)
                }.addOnFailureListener { exception ->
                    Log.e("Download URL Error", "Failed to get download URL: ${exception.message}")
                }
            } else {
                Log.e("Upload Error", "Image upload failed: ${task.exception?.message}")
            }
        }.addOnFailureListener { exception ->
            Log.e("Upload Error", "Image upload task failed: ${exception.message}")
        }
    }

    // Save the uploaded image URL to Firestore
    private fun saveImageUrlToFirestore(email: String, imageUrl: String) {
        val firestore = FirebaseFirestore.getInstance()

        // Parent collection reference
        val parentCollectionRef = firestore.collection("Users")
            .document("parents")
            .collection("newUserParent")

        // Therapist collection reference
        val therapistCollectionRef = firestore.collection("Users")
            .document("therapists")
            .collection("newUserTherapist")

        // check in the parents collection
        parentCollectionRef.whereEqualTo("parentDetails.email", email).get()
            .addOnSuccessListener { parentDocuments ->
                if (!parentDocuments.isEmpty) {
                    val document = parentDocuments.documents[0]
                    val parentDocRef = document.reference

                    val updates = hashMapOf<String, Any>(
                        "parentDetails.imageUrl" to imageUrl
                    )
                    parentDocRef.update(updates)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Image URL added to parentDetails successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore Error", "Failed to add image URL for parent: ${e.message}")
                        }
                } else {
                    // If not found in parents,  find in therapists
                    therapistCollectionRef.whereEqualTo("email", email).get()
                        .addOnSuccessListener { therapistDocuments ->
                            if (!therapistDocuments.isEmpty) {
                                val document = therapistDocuments.documents[0]
                                val therapistDocRef = document.reference

                                val updates = hashMapOf("imageUrl" to imageUrl)

                                therapistDocRef.update(updates as Map<String, Any>)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Image URL added to therapist document successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore Error", "Failed to add image URL for therapist: ${e.message}")
                                    }
                            } else {
                                Log.e("Firestore Error", "No document found for email")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore Error", "Error finding document for therapist: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Error", "Error finding document for parent: ${exception.message}")
            }
    }

}