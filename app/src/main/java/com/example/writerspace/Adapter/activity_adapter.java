package com.example.writerspace.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.model.activities;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static java.text.DateFormat.getDateTimeInstance;

public class activity_adapter extends RecyclerView.Adapter<activity_adapter.ViewHolder>{

    private Context context;
    private List<activities> activitiesList;

    public activity_adapter(Context context, List<activities> activitiesList) {
        this.context=context;
        this.activitiesList=activitiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity,parent,false);
        return new activity_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            activities activities=activitiesList.get(position);
            holder.user_id.setText(activities.getUserid());
            holder.activityid.setText(activities.getActivityid());
            holder.activitytype.setText(activities.getActivitytype());
            holder.postid.setText(activities.getPostid());
            String s=getTimeDate(activities.getTimestamp());
            holder.activitytime.setText(s);
            String userid=activities.getUserid();
            publisherInfo(holder.username,userid);
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView user_id,activityid,activitytype,activitytime,username,postid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_id=itemView.findViewById(R.id.user_id);
            activityid=itemView.findViewById(R.id.activity_id);
            activitytype=itemView.findViewById(R.id.activity_type);
            activitytime=itemView.findViewById(R.id.time_stamp);
            username=itemView.findViewById(R.id.user_name);
            postid=itemView.findViewById(R.id.post_id);
        }
    }
    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
    private void publisherInfo(TextView username,String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user users=snapshot.getValue(com.example.writerspace.model.user.class);
                username.setText(users.getUname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
