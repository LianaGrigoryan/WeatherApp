package com.example.user.weatherapp;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherAPI {
    private String apiKey = "235bef5a99d6bc6193525182c409602c";
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private String units = "metric";
    private static Retrofit retrofit = null;


    public interface ApiInterface {
        @GET("weather")
        Call<WeatherDay> getToday(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("appid") String appid
        );

        @GET("forecast/daily")
        Call<WeatherForecast> getForecast(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("appid") String appid,
                @Query("cnt") int cnt
        );
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
