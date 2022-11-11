package com.example.writerspace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import com.example.writerspace.dialog.CntDialog;
import com.example.writerspace.dialog.prompt_select_dialog;
import com.example.writerspace.dialog.timerDialog;

public class write extends AppCompatActivity implements CntDialog.DialogListener, timerDialog.tDialogListener {
    EditText writings;
    Toolbar toolbar;
    Button wc,timer,proceed,proceedimage;
    TextView counter,timeMins;
    ImageView close;
    CountDownTimer countDownTimer;
    int l;
    Long min;
    String str;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);
        getIntent();
        writings=findViewById(R.id.text);
        toolbar=findViewById(R.id.toolbar);
        counter=findViewById(R.id.counter);
        timeMins=findViewById(R.id.timeMins);
        proceed=findViewById(R.id.proceed);
        close=findViewById(R.id.close_write);
        setSupportActionBar(toolbar);
        wc=findViewById(R.id.wc);
        timer=findViewById(R.id.timer);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(write.this,proceed.class);
                intent.putExtra("TextBox",writings.getText().toString());
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Proceed", Toast.LENGTH_LONG).show();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.writerspace.fragment.feed.class));
            }
        });


        writings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counter.setText("Characters: " + s.length() +"/"+l);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delete:
                Toast.makeText(getApplicationContext(),"Delete Selected", Toast.LENGTH_LONG).show();
                writings.setText("");
                return true;
            case R.id.goals:
                Toast.makeText(getApplicationContext(),"Goals Selected", Toast.LENGTH_LONG).show();
                wc.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                wc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                        counter.setVisibility(View.VISIBLE);
                    }
                });
                timer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timerDiag();
                    }
                });
                return true;
            case R.id.prompt:
                prompt_select_dialog promptSelectDialog=new prompt_select_dialog();
                promptSelectDialog.show(getSupportFragmentManager(),"Prompt");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void timerDiag() {
        timerDialog timerDialog=new timerDialog();
        timerDialog.show(getSupportFragmentManager(),"timerdiag");

    }

    private void openDialog() {
        CntDialog dialog=new CntDialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void applyTexts(int limit) {
        l=limit;
        writings.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
    }



    public void timer(long min){
        timeMins.setVisibility(View.VISIBLE);
        countDownTimer=new CountDownTimer(min*60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long ms=(millisUntilFinished/1000)/60;
                int sm=(int)((millisUntilFinished/1000)%60);
                String value = String.format(ms+":"+sm);
                timeMins.setText(value);
            }

            @Override
            public void onFinish() {
                Toast toast = Toast.makeText(write.this,"Finished!!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 50, 50);
                toast.show();
                MediaPlayer mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.finish);
                mediaPlayer.start();

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(600);
                }

            }
        }.start();

    }


    @Override
    public void timeapplyTexts(long time) {
        min=time;
        timer(min);

    }
}
