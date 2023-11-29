package com.codingstuff.loginandsignup.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingstuff.loginandsignup.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
//        binding.registerPage.setOnClickListener {
//            val intent = Intent(this, NewRegisterActivity::class.java)
//            startActivity(intent)
//        }

        binding.forgor.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {

                    if (it.isSuccessful) {
                        checkUser()
                    } else {
                        Toast.makeText(this, "Email atau Password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Cek format email", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Kolom tidak boleh kosong !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userType = snapshot.child("userType").getValue(String::class.java)

                    if (userType == "user") {
                        val intent = Intent(this@SignInActivity, DashboardUserActivity::class.java)
                        startActivity(intent)
                    } else if (userType == "admin") {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(s: String, s1: String) {
        val s = "admin"
        val s1 = "123"

    }
}