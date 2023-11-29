
package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codingstuff.loginandsignup.Adapter.AdapterPdfFavorite;
import com.codingstuff.loginandsignup.Domain.ModelPDF;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.databinding.ActivityProfileUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileUserActivity extends AppCompatActivity {

    //
    private ActivityProfileUserBinding binding;
    //
    private FirebaseAuth firebaseAuth;
    private  static final String TAG = "PROFILE_TAG";
    //
    private ArrayList<ModelPDF> pdfArrayList;
    //
    private AdapterPdfFavorite adapterPdfFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();
        loadFavoriteBooks();

        //
        binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileUserActivity.this, ProfileEditActivity.class));
            }
        });
    }

    private void loadFavoriteBooks() {
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Favorites")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String bookId = "" + ds.child("bookId").getValue();

                    //
                    ModelPDF modelPDF = new ModelPDF();
                    modelPDF.setId(bookId);

                    //
                    pdfArrayList.add(modelPDF);

                }
                //
                binding.favoriteBookCountTv.setText("" + pdfArrayList.size());   //
                //
                adapterPdfFavorite = new AdapterPdfFavorite(ProfileUserActivity.this, pdfArrayList);
                //
                binding.booksRv.setAdapter(adapterPdfFavorite);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: Loading user info of user "+firebaseAuth.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //
                        String email = ""+snapshot.child("email").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String profileImage = ""+snapshot.child("profileImage").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();
                        String userType = ""+snapshot.child("userType").getValue();

                        //
                        String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                        //
                        binding.emailTv.setText(email);
                        binding.nameTv.setText(name);
                        binding.memberDateTv.setText(formattedDate);
                        binding.accountTypeTv.setText(userType);

                        //
                        Glide.with(getApplicationContext())
                                .load(profileImage)
                                .placeholder(R.drawable.profile)
                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}