package com.example.writerspace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.fragment.feed;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class proceedrecord extends AppCompatActivity {

    SeekBar seekBar;
    Button post,play,stop;
    EditText description,title;
    ImageView back;
    MediaPlayer mediaPlayer;
    TextView seekbarhint;
    public static int oneTimeOnly = 0;
    private double startTime = 0;
    private double finalTime = 0;
    String path;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.proceedrecord);
        super.onCreate(savedInstanceState);
        seekBar=findViewById(R.id.seekbar);
        seekbarhint=findViewById(R.id.seekbarhint);
        post=findViewById(R.id.rpost);
        play=findViewById(R.id.rplay);
        stop=findViewById(R.id.rstop);
        description=findViewById(R.id.rdescription);
        title=findViewById(R.id.rtitle);
        back=findViewById(R.id.back_record);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(proceedrecord.this, record.class));
            }
        });

        mediaPlayer=new MediaPlayer();
        Intent i=getIntent();
        path=i.getStringExtra("audio");
        System.out.println("audiopath"+path);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Audio Files");
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        seekBar.setMax(mediaPlayer.getDuration());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,1000);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(proceedrecord.this,"Playing..",Toast.LENGTH_SHORT).show();
                finalTime=mediaPlayer.getDuration();
                startTime=mediaPlayer.getCurrentPosition();
                if(oneTimeOnly==0){
                    seekBar.setMax((int)finalTime);
                    oneTimeOnly=1;
                }
                seekbarhint.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),TimeUnit.MILLISECONDS.toSeconds((long)finalTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))));
                seekBar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateAudioTime,100);

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                Toast.makeText(proceedrecord.this,"Stoping..",Toast.LENGTH_SHORT).show();

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
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


    private void upload() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");

        if(mediaPlayer!=null)
        {
            Uri uri=Uri.fromFile(new File(path));
            StorageReference filepath=storageReference.child("Audio Files").child(uri.getLastPathSegment());
            Toast.makeText(proceedrecord.this,uri.getLastPathSegment(),Toast.LENGTH_SHORT).show();

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Posts").child("Recordings");
                    String postid=dbref.push().getKey();
                    String desc=description.getText().toString();
                    String audiotitle=title.getText().toString();
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("postid",postid);
                    hashMap.put("audiofile",path);
                    hashMap.put("audiotitle",audiotitle);
                    hashMap.put("description",desc);
                    hashMap.put("publisher",firebaseAuth.getUid());

                    dbref.child(postid).setValue(hashMap);

                    DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("Activities");
                    String activityid=databaseRef.push().getKey();
                    HashMap<String, Object> hashMap1=new HashMap<>();
                    hashMap1.put("userid",firebaseAuth.getUid());
                    hashMap1.put("activityid",activityid);
                    hashMap1.put("activitytype","Recording posted");
                    hashMap1.put("postid",postid);
                    hashMap1.put("timestamp", ServerValue.TIMESTAMP);
                    databaseRef.child(activityid).setValue(hashMap1);

                    progressDialog.dismiss();
                    startActivity(new Intent(proceedrecord.this, feed.class));
                    Toast.makeText(proceedrecord.this,"Posted..",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }
    }

    private Runnable UpdateAudioTime=new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            seekbarhint.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime))));
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this,100);
        }
    };

}
