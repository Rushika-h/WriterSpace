package com.example.writerspace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.fragment.feed;
import com.google.firebase.auth.FirebaseAuth;

public class splashscreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth=FirebaseAuth.getInstance();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(), feed.class));
                }
                else {
                    Intent intent = new Intent(splashscreen.this, login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN_TIME_OUT);

    }
}
