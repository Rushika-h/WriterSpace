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
import com.example.writerspace.Adapter.post_text_adapter;
import com.example.writerspace.R;
import com.example.writerspace.model.post_image;
import com.example.writerspace.model.post_text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notification_text extends Fragment {
    String postid;
    private RecyclerView recyclerView;
    private post_text_adapter postTextAdapter;
    private List<post_text> postTexts;

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

        postTexts=new ArrayList<>();
        postTextAdapter=new post_text_adapter(getContext(),postTexts);
        recyclerView.setAdapter(postTextAdapter);

        readPosts();

        return view;

    }
    private void readPosts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts").child("Writings").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postTexts.clear();
                post_text post_text=snapshot.getValue(post_text.class);
                postTexts.add(post_text);

                postTextAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
