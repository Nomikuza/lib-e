package com.codingstuff.loginandsignup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.codingstuff.loginandsignup.R;

public class IntroActivity extends AppCompatActivity {
    public AppCompatButton introBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introBtn=findViewById(R.id.introBtn);

        introBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,SignInActivity.class));
            }
        });
    }
}
