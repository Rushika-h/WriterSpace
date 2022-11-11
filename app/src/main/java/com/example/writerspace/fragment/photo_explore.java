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
import com.example.writerspace.R;
import com.example.writerspace.model.post_image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class photo_explore extends Fragment {
    private RecyclerView recyclerView;
    private com.example.writerspace.Adapter.post_image_adapter post_image_adapter;
    private List<post_image> postLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag,container,false);

        recyclerView=view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        post_image_adapter = new post_image_adapter(getContext(),postLists);
        recyclerView.setAdapter(post_image_adapter);

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
                    postLists.add(post_image);
                }
                post_image_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
