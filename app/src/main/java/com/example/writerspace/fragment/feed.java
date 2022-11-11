package com.example.writerspace.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.writerspace.R;
import com.example.writerspace.home;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class feed extends AppCompatActivity {
    FirebaseAuth auth;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        auth=FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        Bundle intent=getIntent().getExtras();
        if(intent!=null){
            String publisher=intent.getString("publisherid");
            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profile",publisher);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new profile()).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new home()).commit();
        }
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener onNav= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()){
                case R.id.homebtn:
                    selected= new home();
                    break;
                case R.id.searchbtn:
                    selected= new search();
                    break;
                case R.id.addbtn:
                    selected= new add();
                    break;
                case R.id.notificationbtn:
                    selected= new notification();
                    break;
                case R.id.profilebtn:
                    SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    selected= new profile();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();

        return true;
        }
    };
}
