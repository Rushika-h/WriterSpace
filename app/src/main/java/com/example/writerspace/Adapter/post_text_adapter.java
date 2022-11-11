package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.comments;
import com.example.writerspace.model.post_text;
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

public class post_text_adapter extends RecyclerView.Adapter<post_text_adapter.ViewHolder>{

    public Context context;
    public List<post_text> postTexts;

    private FirebaseUser firebaseUser;

    public post_text_adapter(Context context, List<post_text> postTexts) {
        this.context = context;
        this.postTexts = postTexts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_text,parent,false);
        return new post_text_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        post_text post_text=postTexts.get(position);

        holder.post_txt.setText(post_text.getText());

        if(post_text.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post_text.getDescription());
        }
        publisherInfo(holder.image_profile,holder.username,holder.publisher,post_text.getPublisher());
        isLiked(post_text.getPostid(),holder.like);
        nrLikes(holder.likes,post_text.getPostid());
        getComments(post_text.getPostid(),holder.comments);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_text.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post_text.getPublisher(),post_text.getPostid(),post_text.getTitle());

                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_text.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(), comments.class);
                intent.putExtra("postid",post_text.getPostid());
                intent.putExtra("publisherid",post_text.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comments.class);
                intent.putExtra("postid",post_text.getPostid());
                intent.putExtra("publisherid",post_text.getPublisher());
                context.startActivity(intent);
            }
        });


    }
    private void addNotifications(String userid,String postid,String title){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","liked your post");
        hashMap.put("title",title);
        hashMap.put("postid",postid);
        hashMap.put("image",false);
        hashMap.put("audio",false);
        hashMap.put("writing",true);
        reference.push().setValue(hashMap);
    }


    @Override
    public int getItemCount() {
        return postTexts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,like,comment;
        public TextView username,likes,publisher,description,comments,post_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.w_image_profile);
            post_txt=itemView.findViewById(R.id.post_text);
            like=itemView.findViewById(R.id.wlike);
            likes=itemView.findViewById(R.id.wlikes);
            comment=itemView.findViewById(R.id.wcomment);
            username=itemView.findViewById(R.id.w_username);
            publisher=itemView.findViewById(R.id.wpublisher);
            description=itemView.findViewById(R.id.w_description);
            comments=itemView.findViewById(R.id.wcomments);

        }
    }
    private void getComments(String postid,TextView comments){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    comments.setText("View all " + snapshot.getChildrenCount() + " Comments");
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isLiked(String postid,ImageView imageView){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.filledlike);
                    imageView.setTag("liked");
                }else
                {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void nrLikes(TextView likes,String postid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+"likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void publisherInfo(ImageView image_profile,TextView username,TextView publisher,String userid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(com.example.writerspace.model.user.class);
                Glide.with(context).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUname());
                publisher.setText(user.getUname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
