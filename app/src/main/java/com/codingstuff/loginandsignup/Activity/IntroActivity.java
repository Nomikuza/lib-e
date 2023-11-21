package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.codingstuff.loginandsignup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntroActivity extends AppCompatActivity {
    public AppCompatButton introBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introBtn = findViewById(R.id.introBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        introBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
                checkUser();
            }
        });
    }

    private void checkUser() {
        //
        //
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            //
            //
            startActivity(new Intent(IntroActivity.this, SignInActivity.class));
            finish();
        } else {


            //
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            //
                            String userType = "" + snapshot.child("userType").getValue();
                            //
                            if (userType.equals("user")) {
                                //
                                startActivity(new Intent(IntroActivity.this, DashboardUserActivity.class));
                                finish();
                            } else if (userType.equals("admin")) {
                                //
                                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}
