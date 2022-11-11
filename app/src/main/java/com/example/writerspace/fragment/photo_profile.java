package com.example.writerspace.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.post_image_adapter;
import com.example.writerspace.Adapter.profile_photo;
import com.example.writerspace.R;
import com.example.writerspace.model.post_image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class photo_profile extends Fragment {
    private RecyclerView recyclerView;
    private profile_photo profile_photo;
    private List<post_image> postLists;
    String profileid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag,container,false);

        SharedPreferences preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid=preferences.getString("profileid", FirebaseAuth.getInstance().getUid());
        recyclerView=view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        profile_photo = new profile_photo(getContext(),postLists);
        recyclerView.setAdapter(profile_photo);

        readPosts();
        return view;
    }
    private void readPosts(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts").child("Images");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    post_image post_image=dataSnapshot.getValue(post_image.class);
                    if(post_image.getPublisher().equals(profileid)) {
                        System.out.println("postedby"+post_image.getPublisher());
                        System.out.println("cureent"+profileid);
                        postLists.add(post_image);
                    }
                }
                profile_photo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
