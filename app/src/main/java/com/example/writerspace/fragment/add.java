package com.example.writerspace.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.writerspace.R;
import com.example.writerspace.image;
import com.example.writerspace.record;
import com.example.writerspace.write;

public class add extends Fragment {
    @Nullable
    Button write, record,image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.writeorrecord, container, false);
        write = view.findViewById(R.id.write);
        record = view.findViewById(R.id.record);
        image=view.findViewById(R.id.photo);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), write.class);
                startActivity(intent1);
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getContext(), com.example.writerspace.record.class);
                startActivity(intent2);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(getContext(), com.example.writerspace.image.class);
                startActivity(intent3);
            }
        });
        return view;
    }
}
