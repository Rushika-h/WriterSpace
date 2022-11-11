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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class register extends AppCompatActivity {
    EditText remailid, rpasswd, rcpasswd, rusername;
    TextView memberlog;
    Button regisbtn;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        remailid = findViewById(R.id.r_emailid);
        rpasswd = findViewById(R.id.r_passw);
        rcpasswd = findViewById(R.id.r_conpassw);
        rusername = findViewById(R.id.r_username);
        regisbtn = findViewById(R.id.register);
        memberlog = findViewById(R.id.member);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        memberlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
        regisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = remailid.getText().toString().trim();
                String pass = rpasswd.getText().toString().trim();
                String cpass = rcpasswd.getText().toString().trim();
                String runame = rusername.getText().toString().trim();
                String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(email)) {
                    remailid.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(runame)) {
                    remailid.setError("Username is required");
                    return;
                }
                if (!email.matches(emailPat)) {
                    remailid.setError("Email format is wrong");
                    return;
                }
                if (TextUtils.isEmpty(pass) && TextUtils.isEmpty(cpass)) {
                    rpasswd.setError("Password is required");
                    return;
                }
                if (pass.length() < 8) {
                    rpasswd.setError("Password should contain atleast 8 characters");
                    return;
                }
                if (!cpass.equals(pass)) {
                    rcpasswd.setError("password doesnot match");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();


                            DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("Activities");
                            String activityid=databaseRef.push().getKey();
                            HashMap<String, Object> hashMap1=new HashMap<>();
                            hashMap1.put("userid",userid);
                            hashMap1.put("activityid",activityid);
                            hashMap1.put("activitytype","User Registered");
                            hashMap1.put("postid","none");
                            hashMap1.put("timestamp", ServerValue.TIMESTAMP);
                            databaseRef.child(activityid).setValue(hashMap1);


                            databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("uname",runame.toLowerCase());
                            hashMap.put("bio","");
                            hashMap.put("fullname","");
                            hashMap.put("imageurl","image");
                            hashMap.put("email",email);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(getApplicationContext(), com.example.writerspace.edit_profile.class));
                                        Toast.makeText(register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
    }
}