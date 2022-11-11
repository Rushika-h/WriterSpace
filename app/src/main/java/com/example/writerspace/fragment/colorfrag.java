package com.example.writerspace.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.Adapter.ColorAdapter;
import com.example.writerspace.R;
import com.example.writerspace.Interface.colorfragListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class colorfrag extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    int colorSelected = Color.parseColor("#000000");
    RecyclerView recycler_color;
    EditText textimg;
    Button done;

    colorfragListener listener;

    public void setListener(colorfragListener listener) {
        this.listener = listener;
    }

    static colorfrag instance;

    public static colorfrag getInstance() {
        if (instance==null)
            instance= new colorfrag();
        return instance;
    }

    public colorfrag(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemview=inflater.inflate(R.layout.colorfrag,container,false);

        textimg=itemview.findViewById(R.id.textimg);
        done=itemview.findViewById(R.id.colordone);
        recycler_color=itemview.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        ColorAdapter colorAdapter= new ColorAdapter(getContext(),this);
        recycler_color.setAdapter(colorAdapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.oncolorfragListenerClick(textimg.getText().toString(),colorSelected);

            }
        });

        return itemview;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected=color;
    }
}

