package com.codingstuff.loginandsignup.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.codingstuff.loginandsignup.Transforms.CircleTransform
import com.codingstuff.loginandsignup.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.*

private lateinit var binding: ActivityProfileBinding
private lateinit var auth: FirebaseAuth
private lateinit var user: User
private lateinit var uid: String
private lateinit var databaseReference: DatabaseReference
private lateinit var storageReference: StorageReference

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        showUserProfile()

        binding.logBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.categoryBtn.setOnClickListener {
            startActivity(Intent(this, ShowCategoryActivity::class.java))
        }

        binding.profileImageprof.setOnClickListener {
            startActivity(Intent(this, ProfileUserActivity::class.java))
        }

    }
    private fun showUserProfile() {
//        val uid = auth.currentUser?.uid
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImage = "" + snapshot.child("profileImage").getValue()
                val name = snapshot.child("name").getValue()
                val email = snapshot.child("email").getValue()

                binding.name.setText("Halo! " + name)
                binding.emails.setText("" + email)

                val profileProf = binding.profileImageprof
                Picasso.get().load(profileImage)
                    .resize(600, 200)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(profileProf)
//                getUserProfile()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }


}