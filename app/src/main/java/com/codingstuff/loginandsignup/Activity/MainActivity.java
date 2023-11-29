package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.loginandsignup.Adapter.TrendsAdapter;
import com.codingstuff.loginandsignup.Domain.TrendSDomain;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.Transforms.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTrendsList;
    private RecyclerView recyclerViewTrends;
    private CardView tambahBuku, peminjaman, pengembalian;
    private TextView helloName;
    private ImageView profilePic;
    private ConstraintLayout switchView;
    private FirebaseAuth auth;
    private Uri uriImage;
    private String TAG = "TAGMAIN";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tambahBuku = findViewById(R.id.tambahBuku);
        peminjaman = findViewById(R.id.peminjaman);
        pengembalian = findViewById(R.id.pengembalian);
        profilePic = findViewById(R.id.profilePic);
        helloName = findViewById(R.id.helloName);
        switchView = findViewById(R.id.switchView);

        auth = FirebaseAuth.getInstance();
        String uids = FirebaseAuth.getInstance().getUid();
        Log.d(TAG, uids);

        initRecyclerView();
        BottomNavigation();
        showUserProfile();

        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DashboardUserActivity.class));
            }
        });
        tambahBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadBukuActivity.class));
            }
        });

        peminjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadPeminjamanActivity.class));
            }
        });

        pengembalian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QRScannerPeminjaman.class));
            }
        });

    }

    private void showUserProfile() {
        String userID = auth.getUid();

        //
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
//                Uri uri = firebaseUser.getPhotoUrl();

                String profileImage = "" + snapshot.child("profileImage").getValue();
                String name = "" + snapshot.child("name").getValue();


                //
                helloName.setText("Halo, "+name);
                Picasso.get().load(profileImage)
                        .resize(600, 200)
                        .centerCrop()
                        .transform(new CircleTransform())
                        .into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BottomNavigation() {
        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout categoryBtn = findViewById(R.id.addCategory);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

            }
        });
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UploadCategoryActivity.class));

            }
        });
    }

    private void initRecyclerView() {
        ArrayList<TrendSDomain> items = new ArrayList<>();

        items.add(new TrendSDomain("Fut", "The Nat", "trends"));
        items.add(new TrendSDomain("Future issssss", "Routers", "trends2"));
        items.add(new TrendSDomain("Future in AI, What will tomo?", "The Nat", "trends2"));

        recyclerViewTrends = findViewById(R.id.view1);
        recyclerViewTrends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterTrendsList = new TrendsAdapter(items);
        recyclerViewTrends.setAdapter(adapterTrendsList);
    }
}
