package com.example.user.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

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

import static com.example.user.weatherapp.Fragments.PageFragment3.progressBar;

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
    LinearLayout linearLayout;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

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
        sPref = getSharedPreferences("Data", MODE_PRIVATE);
        editor = sPref.edit();
        api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);
        addListeners();
        if (checkInternetConnection()){
            getWeather();
//            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        }
        else {
            addCurrentWeather(new Gson().fromJson(sPref.getString("currentWeather", ""),WeatherDay.class));
            addForecastWeateher(new Gson().fromJson(sPref.getString("forecastWeather", ""),WeatherForecast.class));
            lastUpdateDate.setText(sPref.getString("currentDate", ""));
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.GONE);
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
                if (checkInternetConnection()) {
                    getWeather();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
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

    public void getWeather(){
        linearLayout.removeAllViews();

        //get current weather
        Call<WeatherDay> callToday = api.getToday(cityName, units, api_key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(TAG, "onResponse");
                WeatherDay cureentData = response.body();

                if (response.isSuccessful()) {
                   addCurrentWeather(cureentData);
                   editor.putString("currentWeather", new Gson().toJson(cureentData));
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
                WeatherForecast data = response.body();
                if (response.isSuccessful()) {
                    addForecastWeateher(data);
                    editor.putString("forecastWeather", new Gson().toJson(data));
                }
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                lastUpdateDate.setText(dateFormat.format(new Date()));
                editor.putString("currentDate", dateFormat.format(new Date()));
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.e(TAG, "onFailure");
                Log.e(TAG, t.toString());
            }
        });
    }

    //function for adding current city's weather
    private void addCurrentWeather(WeatherDay cureentData){
        if (cureentData!=null) {
            String s = cureentData.getDescription();
            citytv.setText(cureentData.getCity().toUpperCase());
            desctv.setText(Character.toUpperCase(s.charAt(0)) + s.substring(1));
            current_temp.setText(cureentData.getTempWithDegree());
            Glide.with(getApplicationContext()).load(cureentData.getIconUrl()).into(current_img);
        }
    }

    // fanction for adding 5 days weather
    private void addForecastWeateher(WeatherForecast data){
        if (data!=null) {
            int d = data.getItems().get(0).getDate().get(Calendar.DAY_OF_WEEK);         //get current day of week
            for (WeatherDay day : data.getItems()) {
                int dayOfWeek = day.getDate().get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != d) {                                                   //checking current day with taken day
                    d = dayOfWeek;
                    addViewDynamically(dayOfWeek, day);
                }
            }
        }
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

    private boolean checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        return  connected;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.apply();
    }
}
