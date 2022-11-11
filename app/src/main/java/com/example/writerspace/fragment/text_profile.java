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

import com.example.writerspace.Adapter.post_text_adapter;
import com.example.writerspace.R;
import com.example.writerspace.model.post_text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class text_profile extends Fragment {
    private RecyclerView recyclerView;
    private com.example.writerspace.Adapter.post_text_adapter post_text_adapter;
    private List<post_text> postTextLists;

    String profileid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_frag, container, false);
        SharedPreferences preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid=preferences.getString("profileid", FirebaseAuth.getInstance().getUid());

        recyclerView = view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postTextLists = new ArrayList<>();
        post_text_adapter = new post_text_adapter(getContext(),postTextLists);
        recyclerView.setAdapter(post_text_adapter);

        readPosts();
        return view;

    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child("Writings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postTextLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    post_text post_text = dataSnapshot.getValue(post_text.class);
                    if(post_text.getPublisher().equals(profileid)) {
                        postTextLists.add(post_text);
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
