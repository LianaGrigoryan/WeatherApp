package com.example.user.weatherapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String cityName = "Yerevan";
    String units = "metric";
    String api_key = "235bef5a99d6bc6193525182c409602c";
    WeatherAPI.ApiInterface api;
    String TAG = "WEATHER";
    TextView citytv, desctv, current_temp, lastUpdateDate;
    ImageView current_img;
    Button refreshButton;
    WeatherForecast data = null;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        linearLayout = findViewById(R.id.dayslayout);
        lastUpdateDate = findViewById(R.id.lastUpdateDate);
        addListeners();
        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);
        getWeather();
    }

    private void addListeners(){
        citytv = findViewById(R.id.citytv);
        desctv = findViewById(R.id.descriptiontv);
        current_temp = findViewById(R.id.currenttemp);
        current_img = findViewById(R.id.currentimage);
        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //get current weather
    public void getWeather(){
        linearLayout.removeAllViews();
        Call<WeatherDay> callToday = api.getToday(cityName, units, api_key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, "onResponse");
                WeatherDay data = response.body();
                Log.d("todayweather",data.getCity());

                if (response.isSuccessful()) {
                    String s = data.getDescription();
                    citytv.setText(data.getCity().toUpperCase());
                    desctv.setText(Character.toUpperCase(s.charAt(0)) + s.substring(1));
                    current_temp.setText(data.getTempWithDegree());
                    Glide.with(getApplicationContext()).load(data.getIconUrl()).into(current_img);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });

        // get weather forecast
        Call<WeatherForecast> callForecast = api.getForecast(cityName, units, api_key, 6);  // cnt get 6 days weather except today
        callForecast.enqueue(new Callback<WeatherForecast>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                Log.e(TAG, "onResponse");
                data = response.body();
                if (response.isSuccessful()) {
                    int d = data.getItems().get(0).getDate().get(Calendar.DAY_OF_WEEK);         //get current day of week
                    for (WeatherDay day : data.getItems()) {
                        int dayOfWeek = day.getDate().get(Calendar.DAY_OF_WEEK);
                        int h = day.getDate().get(Calendar.HOUR_OF_DAY);
                        if (dayOfWeek != d) {                                                   //checking current day with taken day
                            d = dayOfWeek;
                            addViewDynamically(dayOfWeek,day);
                        }
                    }
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    lastUpdateDate.setText(dateFormat.format(new Date()));
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });
    }


    //function add views to middle part dynamically
    private  void addViewDynamically(int dayOfWeek, WeatherDay day){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout daylinearLayout = new LinearLayout(getApplicationContext());
        daylinearLayout.setOrientation(LinearLayout.VERTICAL);
        daylinearLayout.setLayoutParams(layoutParams);
        TextView daytv = new TextView(getApplicationContext());
        TextView maxtemptv = new TextView(getApplicationContext());
        TextView mintemptv = new TextView(getApplicationContext());
        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        daytv.setTextColor(Color.GRAY);
        maxtemptv.setTextColor(Color.BLACK);
        mintemptv.setTextColor(Color.GRAY);
        daytv.setGravity(Gravity.CENTER);
        maxtemptv.setGravity(Gravity.CENTER);
        mintemptv.setGravity(Gravity.CENTER);
        daytv.setText(DateFormatSymbols.getInstance().getShortWeekdays()[dayOfWeek]);
        mintemptv.setText(day.getTempMin());
        maxtemptv.setText(day.getTempMax());
        Glide.with(getApplicationContext()).load(day.getIconUrl()).into(imageView);
        daylinearLayout.addView(daytv, lp);
        daylinearLayout.addView(imageView, lp);
        daylinearLayout.addView(maxtemptv, lp);
        daylinearLayout.addView(mintemptv, lp);
        linearLayout.addView(daylinearLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
