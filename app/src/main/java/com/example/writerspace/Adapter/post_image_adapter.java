package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.comments;
import com.example.writerspace.model.post_image;
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

public class post_image_adapter extends RecyclerView.Adapter<post_image_adapter.ViewHolder>{

    public Context context;
    public List<post_image> post;

    private FirebaseUser firebaseUser;

    public post_image_adapter(Context context, List<post_image> post) {
        this.context = context;
        this.post = post;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_image,parent,false);
        return new post_image_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        post_image post_image=post.get(position);
        Glide.with(context).load(post_image.getPostimage()).into(holder.post_image);

        if(post_image.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post_image.getDescription());
        }
        publisherInfo(holder.image_profile,holder.username,holder.publisher,post_image.getPublisher());
        isLiked(post_image.getPostid(),holder.like);
        nrLikes(holder.likes,post_image.getPostid());
        getComments(post_image.getPostid(),holder.comments);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_image.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post_image.getPublisher(),post_image.getPostid(),post_image.getTitle());
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_image.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comments.class);
                intent.putExtra("postid",post_image.getPostid());
                intent.putExtra("publisherid",post_image.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comments.class);
                intent.putExtra("postid",post_image.getPostid());
                intent.putExtra("publisherid",post_image.getPublisher());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,post_image,like,comment;
        public TextView username,likes,publisher,description,comments;

        public ViewHolder(View itemView){
            super(itemView);

            image_profile=itemView.findViewById(R.id.p_image_profile);
            post_image=itemView.findViewById(R.id.post_img);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            username=itemView.findViewById(R.id.p_username);
            likes=itemView.findViewById(R.id.likes);
            publisher=itemView.findViewById(R.id.publisher);
            description=itemView.findViewById(R.id.p_description);
            comments =itemView.findViewById(R.id.comments);
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
    private void addNotifications(String userid,String postid,String title){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","liked your post");
        hashMap.put("title",title);
        hashMap.put("postid",postid);
        hashMap.put("image",true);
        hashMap.put("audio",false);
        hashMap.put("writing",false);
        reference.push().setValue(hashMap);
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
    private void publisherInfo(ImageView image_profile,TextView username,TextView publisher,String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
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
