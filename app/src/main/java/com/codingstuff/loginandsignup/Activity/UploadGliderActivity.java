package com.codingstuff.loginandsignup.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.Adapter.CourseRVModal;
import com.codingstuff.loginandsignup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UploadGliderActivity extends AppCompatActivity {

    private EditText kdBukuEdt,nmBukuEdt,nmPnlsEdt,nmPenerbitEdt,jumlahEdt;
    private Button btnBuku;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);
        kdBukuEdt = findViewById(R.id.KdbukuET);
        nmBukuEdt = findViewById(R.id.NmbukuET);
        nmPnlsEdt = findViewById(R.id.NmpnlsET);
        nmPenerbitEdt = findViewById(R.id.PenerbitET);
        jumlahEdt = findViewById(R.id.JumlahET);
        btnBuku = findViewById(R.id.addbookET);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kdBuku = kdBukuEdt.getText().toString();
                String nmBuku = nmBukuEdt.getText().toString();
                String nmPnls = nmPnlsEdt.getText().toString();
                String nmPenerbit = nmPenerbitEdt.getText().toString();
                String jumlah = jumlahEdt.getText().toString();

                CourseRVModal courseRVModal = new CourseRVModal(kdBuku,nmBuku,nmPnls,nmPenerbit,jumlah);


            }
        });
    }
}
