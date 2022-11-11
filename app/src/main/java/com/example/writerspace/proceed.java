package com.example.writerspace;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.fragment.feed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class proceed extends AppCompatActivity {
    StorageTask uploadTask;
    StorageReference storageReference;
    Toolbar posttoolbar;
    TextView txtpost;
    ImageView back;
    Button post;
    String hash;
    EditText description,writettitle;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.proceed);
        super.onCreate(savedInstanceState);

        post=findViewById(R.id.post);
        txtpost=findViewById(R.id.txtpost);
        description=findViewById(R.id.description);
        writettitle=findViewById(R.id.writettitle);
        posttoolbar=findViewById(R.id.posttoolbar);
        back=findViewById(R.id.back_write);
        fAuth = FirebaseAuth.getInstance();
        Intent i=getIntent();
        String txt=i.getStringExtra("TextBox");
        txtpost.setText(txt);


        storageReference= FirebaseStorage.getInstance().getReference("posts");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(proceed.this, write.class));
            }
        });
    }
    private void upload(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");

        if(txtpost!=null)
        {

            DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Posts").child("Writings");
            String postid=dbref.push().getKey();
            String desc=description.getText().toString();
            String wrt=writettitle.getText().toString();
            String txt=txtpost.getText().toString();
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("postid",postid);
            hashMap.put("text",txt);
            hashMap.put("title",wrt);
            hashMap.put("description",desc);
            hashMap.put("publisher",fAuth.getUid());


            DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("Activities");
            String activityid=databaseRef.push().getKey();
            HashMap<String, Object> hashMap1=new HashMap<>();
            hashMap1.put("userid",fAuth.getUid());
            hashMap1.put("activityid",activityid);
            hashMap1.put("activitytype","Text posted");
            hashMap1.put("postid",postid);
            hashMap1.put("timestamp", ServerValue.TIMESTAMP);
            databaseRef.child(activityid).setValue(hashMap1);

            dbref.child(postid).setValue(hashMap);
            progressDialog.dismiss();
            startActivity(new Intent(proceed.this,feed.class));
            finish();
        }
        else{
            Toast.makeText(proceed.this,"Failed",Toast.LENGTH_SHORT).show();
        }
    }
}
