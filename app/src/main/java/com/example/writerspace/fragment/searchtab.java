package com.example.writerspace.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.UserAdapter;
import com.example.writerspace.R;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class searchtab extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter useradapter;
    private List<user> mUsers;
    Button explore;
    FrameLayout frameLayout;
    EditText search_bar;
    String sear;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_frag,container,false);

        recyclerView=view.findViewById(R.id.recyc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        sharedPreferences=getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        sear= sharedPreferences.getString("search",null);

        readUsers();

        searchUsers(sear);

        return view;
    }
    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("uname")
                .startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    user user=snapshot.getValue(user.class);
                    mUsers.add(user);
                }
                useradapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUsers(){
        mUsers=new ArrayList<>();
        useradapter=new UserAdapter(getContext(),mUsers,true);
        recyclerView.setAdapter(useradapter);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(sear.equals("")){
                    mUsers.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        user users=snapshot.getValue(com.example.writerspace.model.user.class);
                        String usergetid=users.getId();
                        String current= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(usergetid!=null) {
                            if (!usergetid.equals(current)) {
                                System.out.println("usergetid" + users.getId());
                                System.out.println("curent" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mUsers.add(users);
                            }
                        }
                    }
                    useradapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error is",error.getMessage());
            }
        });
    }

}
