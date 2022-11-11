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
import com.example.writerspace.R;
import com.example.writerspace.model.post_image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notification_image extends Fragment {

    String postid;
    private RecyclerView recyclerView;
    private post_image_adapter postImageAdapter;
    private List<post_image> post_images;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_frag,container,false);

        SharedPreferences preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid=preferences.getString("postid","none");

        recyclerView=view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        post_images=new ArrayList<>();
        postImageAdapter=new post_image_adapter(getContext(),post_images);
        recyclerView.setAdapter(postImageAdapter);

        readPosts();

        return view;
    }

    private void readPosts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts").child("Images").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_images.clear();
                post_image postImage=snapshot.getValue(post_image.class);
                post_images.add(postImage);

                postImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
