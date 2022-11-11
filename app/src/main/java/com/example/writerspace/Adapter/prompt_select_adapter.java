package com.example.writerspace.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.R;
import com.example.writerspace.dialog.prompt_select_dialog;
import com.example.writerspace.fragment.profile;
import com.example.writerspace.model.post_text;
import com.example.writerspace.model.prompt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class prompt_select_adapter extends RecyclerView.Adapter<prompt_select_adapter.ViewHolder> {

    Context context;
    List<prompt> prompts;


    public prompt_select_adapter(Context context, List<prompt> prompts) {
        this.context = context;
        this.prompts = prompts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.prompt,parent,false);
        return new prompt_select_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        prompt prompt=prompts.get(position);
        holder.prompt.setText(prompt.getPrompt());

        }

    @Override
    public int getItemCount() {
        return prompts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView prompt,hashtag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prompt=itemView.findViewById(R.id.prompt_select);
        }
    }
}
