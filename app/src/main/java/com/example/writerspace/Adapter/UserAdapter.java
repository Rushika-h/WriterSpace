package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.fragment.feed;
import com.example.writerspace.fragment.profile;
import com.example.writerspace.home;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private final Context mContext;
    private final List<user> mUsers;
    private final Boolean Frag;
    private FirebaseUser firebaseUser;


    public UserAdapter(Context mContext, List<user> mUsers,Boolean Frag) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.Frag=Frag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final user user=mUsers.get(i);
        viewHolder.username.setText(user.getUname());
        viewHolder.fullname.setText(user.getFullname());
        Glide.with(mContext).load(user.getImageurl()).into(viewHolder.image_profile);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("itemviewed");
                System.out.println("proofid"+user.getId());
                SharedPreferences.Editor editor=mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());
                editor.apply();

                if(Frag){
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new profile()).commit();
                }else
                {

                    Intent intent=new Intent(mContext, feed.class);
                    intent.putExtra("publisherid",user.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    mContext.startActivity(intent);
                }
            }
          });
    }



    @Override
    public int getItemCount() {

        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.username);
            fullname=itemView.findViewById(R.id.fullname);
            image_profile=itemView.findViewById(R.id.image_profile);
        }
    }
            public void onCancelled(@NonNull DatabaseError error) {

            }
}
