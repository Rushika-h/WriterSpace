package com.example.writerspace.fragment;

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
import com.example.writerspace.Adapter.post_text_adapter;
import com.example.writerspace.R;
import com.example.writerspace.model.post_image;
import com.example.writerspace.model.post_text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class text_home extends Fragment {
    private RecyclerView recyclerView;
    private post_text_adapter post_text_adapter;
    private List<post_text> postTextLists;

    private List<String> followingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_frag, container, false);

        recyclerView = view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postTextLists = new ArrayList<>();
        post_text_adapter = new post_text_adapter(getContext(),postTextLists);
        recyclerView.setAdapter(post_text_adapter);

        checkFollowing();
        readPosts();
        return view;

    }


    private void checkFollowing(){
        followingList=new ArrayList<>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for(DataSnapshot  dataSnapshot: snapshot.getChildren()){
                    followingList.add(dataSnapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child("Writings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postTextLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    post_text post_text = dataSnapshot.getValue(post_text.class);
                        for(String id:followingList){
                          if(post_text.getPublisher().equals(id)){
                            postTextLists.add(post_text);
                    }
                    }
                }
                post_text_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
