package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.fragment.audio_profile;
import com.example.writerspace.fragment.notification_audio;
import com.example.writerspace.fragment.notification_image;
import com.example.writerspace.fragment.notification_text;
import com.example.writerspace.fragment.photo_home;
import com.example.writerspace.fragment.photo_profile;
import com.example.writerspace.fragment.profile;
import com.example.writerspace.fragment.text_home;
import com.example.writerspace.fragment.text_profile;
import com.example.writerspace.model.Notification;
import com.example.writerspace.model.post_image;
import com.example.writerspace.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification notification=notifications.get(position);
        String noti=notification.getText();
        holder.text.setText(noti);

        getUserInfo(holder.image_profile,holder.username,notification.getUserid());

        if((notification.isImage())||(notification.isImage())||(notification.isAudio())){
            holder.image_profile.setVisibility(View.VISIBLE);
        }else {
            holder.post_image.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.isImage()){
                    SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostid());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new notification_image()).commit();
                }
                else if(notification.isWriting()) {
                    SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostid());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new notification_text()).commit();
                }
                else if(notification.isAudio())
                {
                    SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getPostid());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new notification_audio()).commit();

                }
                else
                {
                    SharedPreferences.Editor editor=context.getSharedPreferences("PREFS",context.MODE_PRIVATE).edit();
                    editor.putString("postid",notification.getUserid());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new profile()).commit();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,post_image;
        public TextView username,text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.image_profile_noti);
            post_image=itemView.findViewById(R.id.post_notification);
            username=itemView.findViewById(R.id.username_noti);
            text=itemView.findViewById(R.id.comment_noti);
        }
    }
    private void getUserInfo(ImageView imageView,TextView username,String publisherid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(com.example.writerspace.model.user.class);
                Glide.with(context).load(user.getImageurl()).into(imageView);
                username.setText(user.getUname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
