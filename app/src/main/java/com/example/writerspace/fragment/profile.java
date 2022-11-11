package com.example.writerspace.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.Adapter.UserAdapter;
import com.example.writerspace.Adapter.prompt_adapter;
import com.example.writerspace.R;
import com.example.writerspace.activity_log;
import com.example.writerspace.admin_prompt;
import com.example.writerspace.edit_profile;
import com.example.writerspace.login;
import com.example.writerspace.model.post_audio;
import com.example.writerspace.model.post_image;
import com.example.writerspace.model.post_text;
import com.example.writerspace.model.prompt;
import com.example.writerspace.model.user;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class profile extends Fragment {

    ImageView image_profile,logout,report,prompt;
    TextView posts,followers,following,bio,username;
    Button edit_profile;
    Fragment fragment = null;

    FirebaseUser firebaseUser;
    TabLayout tabLayout;
    String profileid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,container,false);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid=preferences.getString("profileid",FirebaseAuth.getInstance().getUid());
        posts=view.findViewById(R.id.posts);
        image_profile=view.findViewById(R.id.image_pro);
        followers=view.findViewById(R.id.followers);
        following=view.findViewById(R.id.following);
        bio=view.findViewById(R.id.bio);
        username=view.findViewById(R.id.username_p);
        edit_profile=view.findViewById(R.id.edit_profile);
        logout=view.findViewById(R.id.logout);
        report=view.findViewById(R.id.report);
        prompt=view.findViewById(R.id.prompt);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        report.setVisibility(View.INVISIBLE);
        prompt.setVisibility(View.INVISIBLE);


        tabLayout=view.findViewById(R.id.tab_Layout);
        userInfo();
        getFollowers();



        getChildFragmentManager().beginTransaction().replace(R.id.profile_frame,new text_profile()).commit();
        getNrPostsText();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new text_profile();
                        getNrPostsText();
                        break;
                    case 1:
                        fragment = new audio_profile();
                        getNrPostsAudio();
                        break;
                    case 2:
                        fragment = new photo_profile();
                        getNrPosts();
                        break;

                }
                getChildFragmentManager().beginTransaction().replace(R.id.profile_frame, fragment).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        }else {
            System.out.println("ELSE");
            checkFollow();
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn=edit_profile.getText().toString();

                if(btn.equals("Edit Profile")){
                    startActivity(new Intent(getContext(), com.example.writerspace.edit_profile.class));

                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("followers").child(firebaseUser.getUid()).setValue(true);
                    addNotifications();
                }else if(btn.equals("following")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),followingz.class);
                intent.putExtra("userid",profileid);
                intent.putExtra("title","followers");
                startActivity(intent);
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),followingz.class);
                intent.putExtra("userid",profileid);
                intent.putExtra("title","following");
                startActivity(intent);

            }
        });
        return view;
    }
  private void addNotifications(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","started following you");
        hashMap.put("postid","");
        hashMap.put("image",false);
        hashMap.put("audio",false);
        hashMap.put("writing",false);
        reference.push().setValue(hashMap);
    }

    private void userInfo(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext()==null){
                    System.out.println("Errorrr");
                    return;
                }
                user user=snapshot.getValue(com.example.writerspace.model.user.class);
                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                System.out.println("usernameEE"+user.getUname());
                username.setText(user.getUname());
                if(user.getBio()!=null){
                bio.setText(user.getBio());
                }
                if(user.getEmail().equals("admin@mail.com")&&user.getUname().equals("admin"))
                {
                    report.setVisibility(View.VISIBLE);
                    prompt.setVisibility(View.VISIBLE);
                    report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), activity_log.class));

                        }
                    });
                    prompt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getContext(), admin_prompt.class));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkFollow(){
        System.out.println("profileid!"+profileid);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                }
                else {
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getFollowers(){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getNrPosts(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child("Images");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    post_image post_image=snapshot1.getValue(com.example.writerspace.model.post_image.class);
                    if (post_image.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getNrPostsText(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child("Writings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    post_text post_text=snapshot1.getValue(com.example.writerspace.model.post_text.class);
                    if (post_text.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getNrPostsAudio(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child("Recordings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    post_audio post_audio=snapshot1.getValue(com.example.writerspace.model.post_audio.class);
                    if (post_audio.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
