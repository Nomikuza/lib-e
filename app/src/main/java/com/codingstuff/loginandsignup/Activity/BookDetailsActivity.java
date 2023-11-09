package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codingstuff.loginandsignup.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookDetailsActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailAuthor, detailPage, detailPenerbit, detailKdBuku, detailJumlah;
    ImageView detailsImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        detailDesc = findViewById(R.id.details_desc);
        detailsImage = findViewById(R.id.item_book_imgDetails);
        detailTitle = findViewById(R.id.item_book_kdPjmDetails);
        detailAuthor = findViewById(R.id.item_book_authorDetailsInp);
        detailPage = findViewById(R.id.item_book_pagesrevDetailsInp);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailPenerbit = findViewById(R.id.item_book_penerbitDetailsInp);
        detailJumlah = findViewById(R.id.item_book_jumlahDetailsInp);
        detailKdBuku = findViewById(R.id.item_book_kdBukuDetailsInp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            //int hal1 = Integer.parseInt(bundle.getString("hal"));
            detailJumlah.setText(bundle.getString("jumlah"));
            detailDesc.setText(bundle.getString("desc"));
            detailTitle.setText(bundle.getString("nmBuku"));
            detailAuthor.setText(bundle.getString("nmPnls"));
            detailPage.setText(bundle.getString("hal"));
            detailKdBuku.setText(bundle.getString("kdBuku"));
            detailPenerbit.setText(bundle.getString("nmPenerbit"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("dataImage");

            Glide.with(this).load(bundle.getString("dataImage")).into(detailsImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(BookDetailsActivity.this,"Terhapus", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(BookDetailsActivity.this, UpdateBukuActivity.class)
                            .putExtra("Jumlah", detailJumlah.getText().toString())
                            .putExtra("nmBuku", detailTitle.getText().toString())
                            .putExtra("dataImage", imageUrl)
                            .putExtra("Key", key)
                            .putExtra("nmPnls", detailAuthor.getText().toString())
                            .putExtra("nmPenerbit", detailPenerbit.getText().toString())
                            .putExtra("hal", detailPage.getText().toString())
                            .putExtra("kdBuku", detailKdBuku.getText().toString())
                            .putExtra("desc", detailDesc.getText().toString());

                    startActivity(intent);
                }
        });
    }
}