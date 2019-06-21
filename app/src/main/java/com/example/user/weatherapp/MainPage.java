package com.example.user.weatherapp;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    Double lat = 40.1776;
    Double lng = 44.5126;
    int id = 616052;
    String units = "metric";
    String api_key = "235bef5a99d6bc6193525182c409602c";
    WeatherAPI.ApiInterface api;
    String TAG = "WEATHER";
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().setTitle(R.string.main_page_name);

        linearLayout = findViewById(R.id.dayslayout);
        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);
        getWeather();
    }

    public void getWeather(){
        Call<WeatherDay> callToday = api.getToday("Yerevan", units, api_key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, "onResponse");
                WeatherDay data = response.body();
                Log.d("todayweather",data.getCity());

                if (response.isSuccessful()) {
//                    tvTemp.setText(data.getCity() + " " + data.getTempWithDegree());
//                    Glide.with(MainActivity.this).load(data.getIconUrl()).into(tvImage);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });

        // get weather forecast
        Call<WeatherForecast> callForecast = api.getForecast("Yerevan", units, api_key ,6);
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.e(TAG, "onResponse");
                WeatherForecast data = response.body();
                //Log.d(TAG,response.toString());

                if (response.isSuccessful()) {
                    int d = data.getItems().get(0).getDate().get(Calendar.DAY_OF_WEEK);             //get current day of week
                    for (WeatherDay day : data.getItems()) {
                        int dayOfWeek = day.getDate().get(Calendar.DAY_OF_WEEK);
                        int h = day.getDate().get(Calendar.HOUR_OF_DAY);
                        if (dayOfWeek != d) {                         //checking current day with taken day
                            d = dayOfWeek;
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                            LinearLayout daylinearLayout = new LinearLayout(getApplicationContext());
                            daylinearLayout.setOrientation(LinearLayout.VERTICAL);
                            daylinearLayout.setLayoutParams(layoutParams);
//                        daylinearLayout.setId(Integer.parseInt(dayOfWeek + "1"));
                            //Log.i("dayofmonth", String.valueOf(Integer.parseInt(dayOfWeek+"layout")));
                            TextView daytv = new TextView(getApplicationContext());
                            TextView maxtemptv = new TextView(getApplicationContext());
                            TextView mintemptv = new TextView(getApplicationContext());
                            ImageView imageView = new ImageView(getApplicationContext());
                            daytv.setId(Integer.parseInt(dayOfWeek + "2"));
                            imageView.setId(Integer.parseInt(dayOfWeek + "3"));
                            maxtemptv.setId(Integer.parseInt(dayOfWeek + "4"));
                            mintemptv.setId(Integer.parseInt(dayOfWeek + "5"));
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            daytv.setTextColor(R.color.middlieLayoutText);
                            maxtemptv.setTextColor(Color.BLACK);
                            mintemptv.setTextColor(R.color.middlieLayoutText);
                            daytv.setGravity(Gravity.CENTER);
                            maxtemptv.setGravity(Gravity.CENTER);
                            mintemptv.setGravity(Gravity.CENTER);
                            daytv.setText(DateFormatSymbols.getInstance().getShortWeekdays()[dayOfWeek]);
                            mintemptv.setText(day.getTempMin());
                            maxtemptv.setText(day.getTempMax());
                            Glide.with(getApplicationContext()).load(day.getIconUrl()).into(imageView);

                            daylinearLayout.addView(daytv, lp);
                            daylinearLayout.addView(imageView,lp);
                            daylinearLayout.addView(maxtemptv,lp);
                            daylinearLayout.addView(mintemptv,lp);
                            linearLayout.addView(daylinearLayout);

                        }
                    }
//                    for (int i=0; i<5; i++) {
//                        WeatherDay day = data.getItems().get(i);
//                        int dayOfWeek = day.getDate().get(Calendar.DAY_OF_WEEK);
//                        //DateFormatSymbols.getInstance().getWeekdays()[ day.getDate().get(Calendar.DAY_OF_WEEK)];
//
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//
//                        LinearLayout daylinearLayout = new LinearLayout(getApplicationContext());
//                        daylinearLayout.setOrientation(LinearLayout.VERTICAL);
//                        daylinearLayout.setLayoutParams(layoutParams);
////                        daylinearLayout.setId(Integer.parseInt(dayOfWeek + "1"));
//                        //Log.i("dayofmonth", String.valueOf(Integer.parseInt(dayOfWeek+"layout")));
//                        TextView daytv = new TextView(getApplicationContext());
//                        TextView maxtemptv = new TextView(getApplicationContext());
//                        TextView mintemptv = new TextView(getApplicationContext());
//                        ImageView imageView = new ImageView(getApplicationContext());
//                        daytv.setId(Integer.parseInt(dayOfWeek + "2"));
//                        imageView.setId(Integer.parseInt(dayOfWeek + "3"));
//                        maxtemptv.setId(Integer.parseInt(dayOfWeek + "4"));
//                        mintemptv.setId(Integer.parseInt(dayOfWeek + "5"));
//                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        //daytv.setLayoutParams(lp);
//                        maxtemptv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        mintemptv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        String d=DateFormatSymbols.getInstance().getWeekdays()[dayOfWeek];
//                        String mt=day.getTempMin();
//                        String mx = day.getTempMax();
//                        daytv.setTextColor(Color.BLACK);
//                        daytv.setText(d);
//                        mintemptv.setText(mt);
//                        maxtemptv.setText(mx);
//                        Glide.with(getApplicationContext()).load(day.getIconUrl()).into(imageView);
//
//                        daylinearLayout.addView(daytv,0,lp);
//                        daylinearLayout.addView(maxtemptv);
//                        daylinearLayout.addView(mintemptv);
//                        daylinearLayout.addView(imageView);
//                        linearLayout.addView(daylinearLayout);
//                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });

    }

}
