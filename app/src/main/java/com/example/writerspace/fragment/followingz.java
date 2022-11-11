package com.example.writerspace.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.UserAdapter;
import com.example.writerspace.R;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class followingz extends AppCompatActivity {
    private String userid,title;
    private List<user> userList;
    private UserAdapter userAdapter;
    FirebaseUser firebaseUser;


    Toolbar toolbar;
    private List<String> useridlist;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following);

        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        title=intent.getStringExtra("title");

        toolbar=findViewById(R.id.toolbar_ff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        //close

        recyclerView=findViewById(R.id.recycler_ff);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList=new ArrayList<>();

        userAdapter= new UserAdapter(getApplicationContext(),userList,false);
        recyclerView.setAdapter(userAdapter);


        useridlist=new ArrayList<>();
        switch (title){
            case "followers":
                followers();
                break;
            case "following":
                following();
                break;
        }

    }

    private void followers(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Follow").child(userid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                useridlist.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    useridlist.add(dataSnapshot.getKey());

                }

                showuser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void following(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Follow").child(userid).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                useridlist.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    useridlist.add(dataSnapshot.getKey());

                }

                showuser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showuser(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    user users=snapshot1.getValue(user.class);
                    for(String userid:useridlist){
                        if(users.getId()!=null) {
                            if (users.getId().equals(userid)) {
                                userList.add(users);
                            }
                        }
                        }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
