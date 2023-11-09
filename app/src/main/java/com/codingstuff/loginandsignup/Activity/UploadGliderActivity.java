package com.codingstuff.loginandsignup.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.Adapter.CourseRVModal;
import com.codingstuff.loginandsignup.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadGliderActivity extends AppCompatActivity {

    private EditText kdBukuEdt,nmBukuEdt,nmPnlsEdt,nmPenerbitEdt,jumlahEdt;
    private Button btnBuku;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);
        kdBukuEdt = findViewById(R.id.kdPeminjamanPjm);
        nmBukuEdt = findViewById(R.id.nmBukuEdt);
        nmPnlsEdt = findViewById(R.id.nmPnlsEdt);
        nmPenerbitEdt = findViewById(R.id.nmPenerbitEdt);
        jumlahEdt = findViewById(R.id.tglPengembalianPjm);
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
