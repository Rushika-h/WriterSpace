package com.example.writerspace.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.writerspace.R;

public class timerDialog extends AppCompatDialogFragment {
    EditText time;
    tDialogListener tlist;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.timerdiag,null);
        builder.setView(view).setTitle("min").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long mins= Long.valueOf(time.getText().toString());
                tlist.timeapplyTexts(mins);
            }
        });
        time=view.findViewById(R.id.mins);
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            tlist= (timerDialog.tDialogListener) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface tDialogListener{
        void timeapplyTexts(long time);

    }
}
