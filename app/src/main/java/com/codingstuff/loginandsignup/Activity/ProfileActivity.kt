package com.codingstuff.loginandsignup.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingstuff.loginandsignup.R
import com.codingstuff.loginandsignup.databinding.ActivityProfileBinding
import com.codingstuff.loginandsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityProfileBinding
private lateinit var auth: FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


    binding.logBtn.setOnClickListener {
        auth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
        }
    }
}