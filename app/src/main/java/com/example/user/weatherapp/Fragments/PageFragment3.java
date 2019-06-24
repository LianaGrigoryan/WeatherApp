package com.example.user.weatherapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.weatherapp.MainActivity;
import com.example.user.weatherapp.R;

public class PageFragment3 extends Fragment {

    public static ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page3, container, false);
        progressBar = view.findViewById(R.id.Progress);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }
}
