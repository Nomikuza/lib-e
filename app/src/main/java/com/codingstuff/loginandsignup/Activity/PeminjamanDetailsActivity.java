package com.codingstuff.loginandsignup.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingstuff.loginandsignup.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PeminjamanDetailsActivity extends AppCompatActivity {

    TextView detailDesc, detailKdPeminjaman, detailKdBuku, detailPage, detailTglPengembalian, detailTglPeminjaman, detailNim;
    ImageView detailsImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman_details);

        detailDesc = findViewById(R.id.pjmDetails_desc);
        detailsImage = findViewById(R.id.item_book_imgDetails);
        detailKdPeminjaman = findViewById(R.id.item_book_kdPjmDetails);
        detailKdBuku = findViewById(R.id.item_book_kdBukuPjmDetailsInp);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailTglPengembalian = findViewById(R.id.item_book_tglPengembalianDetailsInp);
        detailNim = findViewById(R.id.item_book_nimDetailsInp);
        detailTglPeminjaman = findViewById(R.id.item_book_pagesrevDetailsInp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            //int hal1 = Integer.parseInt(bundle.getString("hal"));
            detailNim.setText(bundle.getString("nim"));
            detailDesc.setText(bundle.getString("desc"));
            detailKdPeminjaman.setText(bundle.getString("kdPeminjaman"));
            detailKdBuku.setText(bundle.getString("kdBuku"));
            detailTglPeminjaman.setText(bundle.getString("tglPeminjaman"));
            detailTglPengembalian.setText(bundle.getString("tglPengembalian"));
            key = bundle.getString("key");

        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Peminjaman");
                reference.child(key).removeValue();
                Toast.makeText(PeminjamanDetailsActivity.this,"Terhapus", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeminjamanDetailsActivity.this, UpdatePeminjamanActivity.class)
                        .putExtra("nim", detailNim.getText().toString())
                        .putExtra("kdPeminjaman", detailKdPeminjaman.getText().toString())
                        //.putExtra("dataImage", imageUrl)
                        .putExtra("key", key)
                        .putExtra("tglPeminjaman", detailTglPeminjaman.getText().toString())
                        .putExtra("tglPengembalian", detailTglPengembalian.getText().toString())
                        .putExtra("kdBuku", detailKdBuku.getText().toString());

                startActivity(intent);
            }
        });
    }
}