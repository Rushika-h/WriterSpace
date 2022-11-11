package com.example.writerspace;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.NotificationAdapter;
import com.example.writerspace.Adapter.activity_adapter;
import com.example.writerspace.model.Notification;
import com.example.writerspace.model.activities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class activity_log extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private List<activities> activitiesList;
    private activity_adapter activityAdapter;
    Button startdate,enddate;
    Spinner spinner1;
    String actid;
    RecyclerView recyclerView;
    DatePickerDialog datePicker;
    String start_date,end_date;
    long etimestamp,stimestamp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        startdate=findViewById(R.id.start_date);
        enddate=findViewById(R.id.end_date);
        //option=findViewById(R.id.option);
        recyclerView=findViewById(R.id.recyc_act);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        activitiesList=new ArrayList<>();
        activityAdapter=new activity_adapter(getApplicationContext(),activitiesList);
        recyclerView.setAdapter(activityAdapter);


        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"None","Registered", "Image Posted", "Audio Posted","Text Posted","Commented"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(activity_log.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                start_date=(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                System.out.println("start"+start_date);
                                String str_date=dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = null;
                                try {
                                    date = (Date)formatter.parse(str_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long output=date.getTime()/1000L;
                                String str=Long.toString(output);
                                stimestamp = Long.parseLong(str) * 1000;
                                System.out.println("loongg"+stimestamp);
                                readStartData();
                            }
                        }, year, month, day);
                datePicker.show();

            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(activity_log.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                end_date = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                String end_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date edate = null;
                                try {
                                    edate = (Date) formatter.parse(end_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long eoutput = edate.getTime() / 1000L;
                                String estr = Long.toString(eoutput);
                                etimestamp = Long.parseLong(estr) * 1000;
                                System.out.println("loogg" + etimestamp);
                                readEndData();
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });


    }

    private void readRegister() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("activitytype").equalTo("User Registered");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("activitytype").equalTo("Image posted");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readText(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("activitytype").equalTo("Text posted");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readAudio(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("activitytype").equalTo("Audio posted");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("activitytype").equalTo("Commented");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void read(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readStartData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities");
        Query query = reference.orderByChild("timestamp").startAt(stimestamp);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    activities activities = snapshot1.getValue(activities.class);
                    actid = activities.getActivityid();
                    activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        public void readEndData(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Activities");
        Query query=reference.orderByChild("timestamp").endAt(etimestamp);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                activitiesList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    activities activities = snapshot1.getValue(activities.class);
                    actid=activities.getActivityid();
                  activitiesList.add(activities);
                }

                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                read();
                break;
            case 1:
                readRegister();
                break;
            case 2:
                readImage();
                break;
            case 3:
                readAudio();
                break;
            case 4:
                readText();
                break;
            case 5:
                readComment();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
