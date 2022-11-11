package com.example.writerspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.writerspace.fragment.audio_explore;
import com.example.writerspace.fragment.audio_home;
import com.example.writerspace.fragment.photo_explore;
import com.example.writerspace.fragment.photo_home;
import com.example.writerspace.fragment.text_explore;
import com.example.writerspace.fragment.text_home;
import com.google.android.material.tabs.TabLayout;

public class explore extends Fragment {
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    //Button explore;
    //TextView writer;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.explore, container, false);


        tabLayout = view.findViewById(R.id.tabLay);
        frameLayout = view.findViewById(R.id.exp_frame);
        //writer=view.findViewById(R.id.writer);
        //writer.setText("Explore");
        //explore=view.findViewById(R.id.explore);
        //explore.setVisibility(View.INVISIBLE);

        getChildFragmentManager().beginTransaction().replace(R.id.exp_frame,new text_explore()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Fragment fragment = null;


                switch (tab.getPosition()) {
                    case 0:
                        fragment = new text_explore();
                        break;
                    case 1:
                        fragment= new audio_explore();
                        break;
                    case 2:
                        fragment=new photo_explore();
                        break;

                }
                getChildFragmentManager().beginTransaction().replace(R.id.exp_frame,fragment).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}
