package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UpdatePengembalianActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateTglPgm, updateKdPjm, updateTglPjm, updateKdBuku, updateNim, updateStatus;
    String[] items = {"Nihil", "Telat Pengembalian", "Buku Hilang/Rusak"};
    ArrayAdapter<String> adapterItems;
    String nim, kdPjm, tglPgm, kdBuku, tglPjm, status;
    private String imageUrl;
    String  key, oldImageURL;
    Intent photoPicker;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembalian_update);

        updateButton = findViewById(R.id.updatebuttonET);
        updateKdPjm = findViewById(R.id.updateKdpeminjamanET);
        updateImage = findViewById(R.id.updateImage);
        updateTglPjm = findViewById(R.id.updateTglPjmET);
        updateTglPgm = findViewById(R.id.updateTglPgmET);
        updateKdBuku = findViewById(R.id.updateKdbukuET);
        updateNim = findViewById(R.id.updatenimET);
        updateStatus = findViewById(R.id.auto_complete_txt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //Glide.with(UpdateBukuActivity.this).load(bundle.getString("dataImage")).into(updateImage);
            updateKdPjm.setText(bundle.getString("kdPeminjaman"));
            updateKdBuku.setText(bundle.getString("kdBuku"));
            updateNim.setText(bundle.getString("nim"));
            updateTglPjm.setText(bundle.getString("tglPeminjaman"));
            updateTglPgm.setText(bundle.getString("tglPengembalian"));
            updateStatus.setText(bundle.getString("status"));
            key = bundle.getString("key");
            //oldImageURL = bundle.getString("Images");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Peminjaman");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kdPjm = updateKdPjm.getText().toString().trim();
                tglPjm = updateTglPjm.getText().toString().trim();
                tglPgm = updateTglPgm.getText().toString().trim();
                nim = updateNim.getText().toString().trim();
                kdBuku = updateKdBuku.getText().toString().trim();
                status = updateStatus.getText().toString().trim();

                updateData(kdPjm,kdBuku,nim,tglPjm,tglPgm,status);

                Intent intent = new Intent(UpdatePengembalianActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateData(String kdPjm, String kdBuku, String nim, String tglPjm, String tglPgm, String status) {


        //  pending
        Map<String, Object> map = new HashMap<>();

        map.put("kdBuku",kdBuku);
        map.put("kdPeminjaman",kdPjm);
        map.put("nim",nim);
        map.put("status",status);
        map.put("tglPeminjaman",tglPjm);
        map.put("tglPengembalian",tglPgm);
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Peminjaman");
        databaseReference.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //updateKdPjm.setText("");
                    Toast.makeText(UpdatePengembalianActivity.this,"Diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                } else  {
                    Toast.makeText(UpdatePengembalianActivity.this,"Gagal Update", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}