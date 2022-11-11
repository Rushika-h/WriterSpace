package com.example.writerspace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class record extends AppCompatActivity {
    Toolbar rtoolbar;
    Button record,start,stopplay,stop,play,proceed;
    ImageView close;
    String pathSave="";
    Random random;
    Chronometer chronometer;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE=1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        getIntent();
        rtoolbar = findViewById(R.id.rectoolbar);
        setSupportActionBar(rtoolbar);
        record=findViewById(R.id.recordbtn);
        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);
        stopplay=findViewById(R.id.stopplay);
        play=findViewById(R.id.play);
        proceed=findViewById(R.id.proceedrec);
        chronometer=findViewById(R.id.rec_timer);
        close=findViewById(R.id.close_record);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.writerspace.fragment.feed.class));
            }
        });

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        play.setEnabled(false);
        stopplay.setEnabled(false);
        random= new Random();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(record.this,proceedrecord.class);
                intent.putExtra("audio",pathSave);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Proceed", Toast.LENGTH_LONG).show();
            }
        });

            record.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(checkPermissionFromDevice())
                    {
                       pathSave= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+CreateRandomAudioFileName(5)+"_audio_record.mpeg4";
                        setupMediaRecorder();

                        try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    }catch (IllegalStateException | IOException e){
                        e.printStackTrace();
                    }
                        play.setEnabled(false);
                        stopplay.setEnabled(true);
                        Toast.makeText(record.this,"Recording...",Toast.LENGTH_SHORT).show();
                }
                    else
                    {
                        requestPermissions();
                    }
                }

            });
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaRecorder.stop();
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    record.setEnabled(true);
                    stopplay.setEnabled(false);
                    chronometer.stop();
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopplay.setEnabled(true);
                    stop.setEnabled(false);
                    record.setEnabled(false);

                    mediaPlayer=new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(record.this,"Playing..",Toast.LENGTH_SHORT).show();
                }
            });
            stopplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop.setEnabled(false);
                    record.setEnabled(false);
                    stopplay.setEnabled(false);
                    play.setEnabled(true);
                    chronometer.stop();

                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
            });

       }

    private String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder=new StringBuilder(string);
        int i=0;
        while (i<string){
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++;
        }
        return stringBuilder.toString();
    }

    private void setupMediaRecorder() {
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
         } catch (IOException e) {
            e.printStackTrace();
            Log.e("start", "prepare() failed");
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0){
                    boolean StoragePermission=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (StoragePermission && RecordPermission){
                        Toast.makeText(this,"Permission",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result==PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rec_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                chronometer.setText("00:00");
                File file=new File(pathSave);
                file.delete();
                Toast.makeText(getApplicationContext(), "Delete Selected", Toast.LENGTH_LONG).show();
                record.setEnabled(true);
                stop.setEnabled(true);
        }
        return super.onOptionsItemSelected(item);

    }
}