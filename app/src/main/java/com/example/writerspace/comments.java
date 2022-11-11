package com.example.writerspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.Adapter.CommentAdapter;
import com.example.writerspace.model.Comment;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class comments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    EditText addcomment;
    ImageView image_profile;
    TextView post;

    String postid,key;
    String publisherid;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        Toolbar toolbar=findViewById(R.id.comment_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.comment_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList=new ArrayList<>();
        commentAdapter=new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);

        addcomment=findViewById(R.id.add_comment);
        image_profile=findViewById(R.id.comment_image_profile);
        post=findViewById(R.id.post_comment);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent=getIntent();
        postid=intent.getStringExtra("postid");
        publisherid=intent.getStringExtra("publisherid");
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(comments.this,"Comments can't be sent empty",Toast.LENGTH_LONG).show();
                }
                else {
                    addComment();
                }
            }
        });
        getImage();
        readComments();
    }

    private void addComment() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        //DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Comments");
        key=reference.push().getKey();
        /*SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("profileid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.apply();
*/
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("comment",addcomment.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());
        reference.child(key).setValue(hashMap);
        addcomment.setText("");

        DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("Activities");
        String activityid=databaseRef.push().getKey();
        HashMap<String, Object> hashMap1=new HashMap<>();
        hashMap1.put("userid",firebaseUser.getUid());
        hashMap1.put("activityid",activityid);
        hashMap1.put("activitytype","Commented");
        hashMap1.put("postid",key);
        hashMap1.put("timestamp", ServerValue.TIMESTAMP);
        databaseRef.child(activityid).setValue(hashMap1);

        addNotifications();
        }
    private void addNotifications(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","commented: "+addcomment.getText().toString());
        hashMap.put("postid",postid);
        hashMap.put("ispost",true);
        reference.push().setValue(hashMap);
    }

    private void getImage(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(com.example.writerspace.model.user.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readComments(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Comment comment=dataSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
