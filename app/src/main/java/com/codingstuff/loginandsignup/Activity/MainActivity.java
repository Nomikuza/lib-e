package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.loginandsignup.Adapter.TrendsAdapter;
import com.codingstuff.loginandsignup.Domain.TrendSDomain;
import com.codingstuff.loginandsignup.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTrendsList;
    private RecyclerView recyclerViewTrends;
    private CardView tambahBuku, peminjaman, pengembalian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tambahBuku = findViewById(R.id.tambahBuku);
        peminjaman = findViewById(R.id.peminjaman);
        pengembalian = findViewById(R.id.pengembalian);


        initRecyclerView();
        BottomNavigation();

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


    private void BottomNavigation() {
        LinearLayout profileBtn=findViewById(R.id.profileBtn);
        LinearLayout categoryBtn=findViewById(R.id.addCategory);
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
