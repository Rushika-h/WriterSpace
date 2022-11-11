package com.example.writerspace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.prompt_adapter;
import com.example.writerspace.fragment.feed;
import com.example.writerspace.model.prompt;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class admin_prompt extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    EditText promptedit;
    ImageView next;
    private List<prompt> prompts;
    private prompt_adapter promptAdapter;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_recyc);

        prompts=new ArrayList<>();
        fab=findViewById(R.id.add_fab);
        recyclerView=findViewById(R.id.admin_recy);
        next=findViewById(R.id.next);
        promptAdapter= new prompt_adapter(this,prompts);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(promptAdapter);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        readprompts();
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                addInfo();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), feed.class));
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addInfo(){
        LayoutInflater inflater=getLayoutInflater().from(this);
        View view=inflater.inflate(R.layout.add_item_admin,null);
        promptedit=view.findViewById(R.id.prompt_edit);
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Prompts");
                String promptid=reference.push().getKey();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("promptid",promptid);
                hashMap.put("prompt",promptedit.getText().toString());

                reference.child(promptid).setValue(hashMap);

                Toast.makeText(getApplicationContext(),"Adding",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_LONG).show();
            }
        });
        dialog.create();
        dialog.show();
    }
    public void readprompts()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Prompts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prompts.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    prompt promptss=dataSnapshot.getValue(prompt.class);
                    prompts.add(promptss);
                }
                promptAdapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
