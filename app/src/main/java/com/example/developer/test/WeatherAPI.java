package com.example.developer.test;

import com.example.developer.test.model.current.Current;
import com.example.developer.test.model.several.SevenDays;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherAPI {
    @GET("/data/2.5/weather")
    Call<Current> getCurrentWeather(@Query("q") String id, @Query("units") String units, @Query("appid") String appid);

    @GET("/data/2.5/forecast/daily")
    Call<SevenDays> getSevenDaysWeather(@Query("q") String id, @Query("units") String units, @Query("appid") String appid);
}
