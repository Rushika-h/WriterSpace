package com.example.writerspace.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.R;
import com.example.writerspace.model.prompt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class prompt_adapter extends RecyclerView.Adapter<prompt_adapter.ViewHolder>{


    Context context;
    List<prompt> prompts;

    private FirebaseUser firebaseUser;


    public prompt_adapter(Context context, List<prompt> prompts) {
        this.context = context;
        this.prompts = prompts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.admin_item,parent,false);
        return new prompt_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        prompt prompt=prompts.get(position);
        holder.prompt.setText(prompt.getPrompt());
        holder.promp=prompt.getPromptid();

    }

    @Override
    public int getItemCount() {
        return prompts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView prompt;
        String promp;
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prompt=itemView.findViewById(R.id.promp);
            remove=itemView.findViewById(R.id.remove);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeprompt(promp);
                }
            });
        }
    }

    private void removeprompt(String promptid) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Prompts").child(promptid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                reference.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
