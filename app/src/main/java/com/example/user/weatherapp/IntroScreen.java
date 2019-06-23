package com.example.user.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.weatherapp.Fragments.PageFragment1;
import com.example.user.weatherapp.Fragments.PageFragment2;

import java.util.ArrayList;
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

        pager = findViewById(R.id.pager);
        android.support.v4.app.FragmentManager f= getSupportFragmentManager();
        fragmentAdapter = new SlidePagerAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(fragmentAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
