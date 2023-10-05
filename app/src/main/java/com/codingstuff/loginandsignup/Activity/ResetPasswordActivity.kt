package com.codingstuff.loginandsignup.Activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingstuff.loginandsignup.R
import com.codingstuff.loginandsignup.databinding.ActivityResetPasswordBinding
import com.codingstuff.loginandsignup.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var etEmailReset: EditText
    private lateinit var btnResetPassword: Button

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etEmailReset = findViewById(R.id.resetemailEt)
        btnResetPassword = findViewById(R.id.btnResetET)
        auth = FirebaseAuth.getInstance()

        binding.textRest.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        btnResetPassword.setOnClickListener{
            compareEmail(etEmailReset)
        }

    }
    //Diluar OnCreate
    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            Toast.makeText(this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            Toast.makeText(this, "Cek format email!", Toast.LENGTH_SHORT).show()
            return
        }
        auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Cek Email!", Toast.LENGTH_SHORT).show()
            }
        }


    }
}