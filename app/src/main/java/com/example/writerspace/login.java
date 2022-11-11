package com.example.writerspace;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.fragment.feed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {

    EditText uemailid,pass;
    Button loginbtn;
    TextView regis;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    private FirebaseAnalytics mFirebaseAnalytics;

    String adminid = "admin@mail.com";
    String adminpass = "admin123";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        fAuth=FirebaseAuth.getInstance();
        uemailid=findViewById(R.id.emailid);
        pass=findViewById(R.id.passw);
        loginbtn=findViewById(R.id.login);
        regis=findViewById(R.id.create);
        progressBar=findViewById(R.id.progressBar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email=uemailid.getText().toString().trim();
                String passwd=pass.getText().toString().trim();
                String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(TextUtils.isEmpty(email))
                {
                    uemailid.setError("Email is required");
                    return;
                }
                if(!email.matches(emailPat))
                {
                    uemailid.setError("Email format is wrong");
                    return;
                }
                if(TextUtils.isEmpty(passwd))
                {
                    pass.setError("Password is required");
                    return;
                }
                if(passwd.length()<8)
                {
                    pass.setError("Password should contain atleast 8 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);


                    fAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), feed.class));

                            } else {
                                Toast.makeText(login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });

            }
        });


    }
}
