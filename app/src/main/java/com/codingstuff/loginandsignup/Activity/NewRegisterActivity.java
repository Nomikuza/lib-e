package com.codingstuff.loginandsignup.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewRegisterActivity extends AppCompatActivity {

    //
    private ActivitySignUpBinding binding;
    //
    private FirebaseAuth firebaseAuth;
    //
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        firebaseAuth = FirebaseAuth.getInstance();

        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tolong ditunggu");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String name = "", email = "", password = "";

    private void validateData() {
//
        //
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passET.getText().toString().trim();
        String cPassword = binding.confirmPassEt.getText().toString().trim();
        
        //
        if (TextUtils.isEmpty(name)){

            Toast.makeText(this, "Ketik nama...", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            Toast.makeText(this, "Cek format email...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Ketik password...!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {

            Toast.makeText(this, "Password tidak cocok...!", Toast.LENGTH_SHORT).show();
        }
        else  {

            createUserAccount();
        }
    }

    private void createUserAccount() {
        //
        progressDialog.setMessage("Membuat akun...");
        progressDialog.show();

        //
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                        progressDialog.dismiss();
                        Toast.makeText(NewRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateUserInfo() {
        progressDialog.setMessage("Menyimpan data akun...");

        //
        long timestamp = System.currentTimeMillis();

        //
        String uid = firebaseAuth.getUid();

        //
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("profileImage", "");    //
        hashMap.put("userType", "user");    //
        hashMap.put("timestamp", timestamp);

        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //
                        progressDialog.dismiss();
                        Toast.makeText(NewRegisterActivity.this, "Sukses mendaftar...", Toast.LENGTH_SHORT).show();
                        //
                        startActivity(new Intent(NewRegisterActivity.this, DashboardUserActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                        progressDialog.dismiss();
                        Toast.makeText(NewRegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}