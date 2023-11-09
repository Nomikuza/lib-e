package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PengembalianDetailsActivity extends AppCompatActivity {

    TextView detailDesc, detailKdPeminjaman, detailKdBuku, detailPage, detailTglPengembalian, detailTglPeminjaman, detailNim, detailStatus;
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
        detailStatus = findViewById(R.id.item_book_statusPjmDetailsInp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            //int hal1 = Integer.parseInt(bundle.getString("hal"));
            detailNim.setText(bundle.getString("nim"));
            detailDesc.setText(bundle.getString("desc"));
            detailKdPeminjaman.setText(bundle.getString("kdPeminjaman"));
            detailKdBuku.setText(bundle.getString("kdBuku"));
            detailTglPeminjaman.setText(bundle.getString("tglPeminjaman"));
            detailTglPengembalian.setText(bundle.getString("tglPengembalian"));
            detailStatus.setText(bundle.getString("status"));
            key = bundle.getString("key");

        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Peminjaman");
                reference.child(key).removeValue();
                Toast.makeText(PengembalianDetailsActivity.this,"Terhapus", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PengembalianDetailsActivity.this, UpdatePengembalianActivity.class)
                        .putExtra("nim", detailNim.getText().toString())
                        .putExtra("kdPeminjaman", detailKdPeminjaman.getText().toString())
                        //.putExtra("dataImage", imageUrl)
                        .putExtra("key", key)
                        .putExtra("tglPeminjaman", detailTglPeminjaman.getText().toString())
                        .putExtra("tglPengembalian", detailTglPengembalian.getText().toString())
                        .putExtra("kdBuku", detailKdBuku.getText().toString())
                        .putExtra("status", detailStatus.getText().toString());

                startActivity(intent);
            }
        });
    }
}