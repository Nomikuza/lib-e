package com.codingstuff.loginandsignup.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.loginandsignup.Domain.ModelCategory;
import com.codingstuff.loginandsignup.databinding.ActivityShowCategoryBinding;
import com.codingstuff.loginandsignup.recyclerview.AdapterCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowCategoryActivity extends AppCompatActivity {

    private ActivityShowCategoryBinding binding;
    private AdapterCategory adapterCategory;
    public ArrayList<ModelCategory> mdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mdata = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowCategoryActivity.this,RecyclerView.VERTICAL,false);
        binding.rvBook.setLayoutManager(layoutManager);
        adapterCategory = new AdapterCategory(ShowCategoryActivity.this,mdata);
        binding.rvBook.setAdapter(adapterCategory);

        loadCategories();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCategory.getFilter().filter(s);
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadCategories() {
        mdata = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Android Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mdata.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    mdata.add(model);
                }
                adapterCategory = new AdapterCategory(ShowCategoryActivity.this, mdata);
                binding.rvBook.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
