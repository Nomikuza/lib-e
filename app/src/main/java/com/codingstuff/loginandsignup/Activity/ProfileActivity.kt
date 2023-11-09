package com.codingstuff.loginandsignup.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codingstuff.loginandsignup.R
import com.codingstuff.loginandsignup.databinding.ActivityProfileBinding
import com.codingstuff.loginandsignup.databinding.ActivityShowCategoryBinding
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

        binding.daftarBuku.setOnClickListener {
            startActivity(Intent(this, ShowBukuActivity::class.java))
        }

        binding.daftarPengembalian.setOnClickListener {
            startActivity(Intent(this, ShowPengembalianActivity::class.java))
        }

        binding.peminjaman.setOnClickListener {
            startActivity(Intent(this, ShowPeminjamanActivity::class.java))
        }

        binding.categoryBtn.setOnClickListener {
            startActivity(Intent(this, ShowCategoryActivity::class.java))
        }

        binding.daftarPdf.setOnClickListener {
            startActivity(Intent(this, PdfListAdminActivity::class.java))
        }

    }
}