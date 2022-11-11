package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.writerspace.R;
import com.example.writerspace.comments;
import com.example.writerspace.model.post_audio;
import com.example.writerspace.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class post_audio_adapter extends RecyclerView.Adapter<post_audio_adapter.ViewHolder>{

    public static int oneTimeOnly = 0;
    private double startTime = 0;
    private double finalTime = 0;

    public Context context;
    public List<post_audio> post_audios;

    private FirebaseUser firebaseUser;

    public post_audio_adapter(Context context, List<com.example.writerspace.model.post_audio> post_audios) {
        this.context = context;
        this.post_audios = post_audios;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.post_record,parent,false);
        return new post_audio_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        post_audio post_audio=post_audios.get(position);

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    holder.mediaPlayer.reset();
                    holder.mediaPlayer.setDataSource(post_audio.getAudiofile());
                    holder.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    holder.mediaPlayer.prepare();
                    holder.mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finalTime=holder.mediaPlayer.getDuration();
                startTime=holder.mediaPlayer.getCurrentPosition();
                if(oneTimeOnly==0){
                    holder.seekBar.setMax((int)finalTime);
                    oneTimeOnly=1;
                }
                holder.seekbarHint.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))));
                holder.seekBar.setProgress((int)startTime);
                holder.handler.postDelayed(holder.UpdateAudioTime,100);

                holder.seekBar.setMax(holder.mediaPlayer.getDuration());
                holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                        if(input){
                            holder.mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }

        });
        holder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mediaPlayer.stop();
                holder.seekBar.setProgress(0);
                holder.mediaPlayer.reset();
            }
        });

        if(post_audio.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post_audio.getDescription());
        }
        publisherInfo(holder.image_profile,holder.username,holder.publisher,post_audio.getPublisher());
        isLiked(post_audio.getPostid(),holder.like);
        nrLikes(holder.likes,post_audio.getPostid());
        getComments(post_audio.getPostid(),holder.comments);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_audio.getPostid()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post_audio.getPublisher(),post_audio.getPostid(),post_audio.getAudiotitle());
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post_audio.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(), comments.class);
                intent.putExtra("postid",post_audio.getPostid());
                intent.putExtra("publisherid",post_audio.getPublisher());
                context.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, comments.class);
                intent.putExtra("postid",post_audio.getPostid());
                intent.putExtra("publisherid",post_audio.getPublisher());
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
        hashMap.put("audio",true);
        hashMap.put("writing",false);
        reference.push().setValue(hashMap);
    }


    @Override
    public int getItemCount() {
        return post_audios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile,like,comment;
        public TextView username,likes,publisher,description,comments,seekbarHint;
        public SeekBar seekBar;
        public Button play,stop;
        MediaPlayer mediaPlayer;
        Handler handler=new Handler();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile=itemView.findViewById(R.id.r_image_profile);
            like=itemView.findViewById(R.id.rlike);
            comment=itemView.findViewById(R.id.rcomment);
            username=itemView.findViewById(R.id.r_username);
            likes=itemView.findViewById(R.id.rlikes);
            publisher=itemView.findViewById(R.id.rpublisher);
            description=itemView.findViewById(R.id.r_description);
            comments=itemView.findViewById(R.id.rcomments);
            seekBar=itemView.findViewById(R.id.rseekbar);
            seekbarHint=itemView.findViewById(R.id.rseekbarhint);
            play=itemView.findViewById(R.id.recplay);
            stop=itemView.findViewById(R.id.recstop);
            mediaPlayer=new MediaPlayer();

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            },0,1000);

        }

        private Runnable UpdateAudioTime =new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            seekbarHint.setText(String.format("%d min, %d sec",TimeUnit.MILLISECONDS.toMinutes((long) startTime),TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }
};

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
