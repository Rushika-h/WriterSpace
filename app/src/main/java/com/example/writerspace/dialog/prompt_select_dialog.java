package com.example.writerspace.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.prompt_adapter;
import com.example.writerspace.Adapter.prompt_select_adapter;
import com.example.writerspace.R;
import com.example.writerspace.model.prompt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class prompt_select_dialog extends AppCompatDialogFragment {

    private List<prompt> promptList;
    RecyclerView recyclerView;
    private prompt_select_adapter promptSelectAdapter;
    FirebaseUser firebaseUser;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.home_frag,null);

        promptList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyc);
        promptSelectAdapter= new prompt_select_adapter(getContext(),promptList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(promptSelectAdapter);

        readPrompts();



        builder.setView(view).setTitle("Prompt").setPositiveButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getContext(),"Close!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private void readPrompts() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Prompts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promptList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    prompt promptss=dataSnapshot.getValue(prompt.class);
                    promptList.add(promptss);
                }
                promptSelectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
