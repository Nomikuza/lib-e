package com.codingstuff.loginandsignup.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.recyclerview.BookAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowBukuActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private List<DataClass> mdata;
    private SearchView searchView;
    private BookAdapter adapter;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_buku);

        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        rvBooks = findViewById(R.id.rv_book);
        mdata = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        adapter = new BookAdapter(mdata, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvBooks.setLayoutManager(linearLayoutManager);
        rvBooks.addItemDecoration(new DividerItemDecoration(rvBooks.getContext(), DividerItemDecoration.VERTICAL));
        rvBooks.setNestedScrollingEnabled(false);
        rvBooks.setAdapter(adapter);

        rvBooks.setHasFixedSize(true);

        firebaseDatabase.getReference().child("Android Tutorials").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mdata.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    DataClass projectModel = dataSnapshot.getValue(DataClass.class);
                    projectModel.setKey(dataSnapshot.getKey());
                    mdata.add(projectModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  pending
                searchList(newText);
                return true;
            }
        });
    }
    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: mdata){
            if (dataClass.getNmBuku().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

}
