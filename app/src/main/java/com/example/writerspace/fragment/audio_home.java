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

import com.example.writerspace.Adapter.post_audio_adapter;
import com.example.writerspace.Adapter.post_image_adapter;
import com.example.writerspace.Adapter.post_text_adapter;
import com.example.writerspace.R;
import com.example.writerspace.model.post_audio;
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

public class audio_home extends Fragment {
    private RecyclerView recyclerView;
    private post_audio_adapter post_audio_adapter;
    private List<post_audio> post_audios;

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
        post_audios = new ArrayList<>();
        post_audio_adapter = new post_audio_adapter(getContext(),post_audios);
        recyclerView.setAdapter(post_audio_adapter);

        checkFollowing();

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child("Recordings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_audios.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    post_audio post_audio = dataSnapshot.getValue(post_audio.class);
                        for(String id:followingList){
                          if(post_audio.getPublisher().equals(id)){
                              post_audios.add(post_audio);
                    }
                    }
                }
                post_audio_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
