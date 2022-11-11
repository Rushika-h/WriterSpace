package com.example.writerspace.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerspace.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewholder> {

    Context context;
    List<Integer> colorlist;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorlist = genColorlist();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewholder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorlist.get(position));
    }

    @Override
    public int getItemCount() {
        return colorlist.size();
    }

    public class ColorViewholder extends RecyclerView.ViewHolder{

        public CardView color_section;

        public ColorViewholder(View itemView){
            super(itemView);
            color_section=(CardView)itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onColorSelected(colorlist.get(getAdapterPosition()));
                }
            });
        }
    }
    private List<Integer> genColorlist(){
        List<Integer> colorlist=new ArrayList<>();
        colorlist.add(Color.parseColor("#000000"));
        colorlist.add(Color.parseColor("#FFFFFF"));
        colorlist.add(Color.parseColor("#FF0000"));
        colorlist.add(Color.parseColor("#00FF00"));
        colorlist.add(Color.parseColor("#0000FF"));
        colorlist.add(Color.parseColor("#FFFF00"));
        colorlist.add(Color.parseColor("#00FFFF"));
        colorlist.add(Color.parseColor("#FF00FF"));
        return colorlist;
    }
    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
