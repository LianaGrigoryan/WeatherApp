package com.example.user.weatherapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.weatherapp.Fragments.PageFragment1;
import com.example.user.weatherapp.Fragments.PageFragment2;
import com.example.user.weatherapp.Fragments.PageFragment3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class IntroScreen extends AppCompatActivity {

    ViewPager pager;
    FragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        List<Fragment> list = new ArrayList<>();
        list.add(new PageFragment1());
        list.add(new PageFragment2());
        list.add(new PageFragment3());

        pager = findViewById(R.id.pager);
        fragmentAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(fragmentAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
