package com.example.writerspace;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.writerspace.fragment.audio_home;
import com.example.writerspace.fragment.photo_home;
import com.example.writerspace.fragment.text_home;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.tabs.TabLayout;

public class home extends Fragment {
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    TextView writer;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.home, container, false);


        tabLayout = view.findViewById(R.id.tabLayout);
        frameLayout = view.findViewById(R.id.home_frame);
        writer=view.findViewById(R.id.writer);
        writer.setText("Writer's Space");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            writer.setTextAppearance(R.style.fontstyle);
        }


        getChildFragmentManager().beginTransaction().replace(R.id.home_frame,new text_home()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new text_home();
                        break;
                    case 1:
                        fragment= new audio_home();
                        break;
                    case 2:
                        fragment=new photo_home();
                        break;

                }
                getChildFragmentManager().beginTransaction().replace(R.id.home_frame,fragment).commit();


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
