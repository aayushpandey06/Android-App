package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {
    private RelativeLayout homeRl;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private ProgressBar loadingPB;

    private RecyclerView weatherRv;

    private ImageView backIV,iconIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_weather);

        homeRl = findViewById(R.id.idRlHome);
        cityNameTV = findViewById(R.id.CityName);
        temperatureTV = findViewById(R.id.Temperature);
        conditionTV = findViewById(R.id.Condition);
        loadingPB = findViewById(R.id.Loading);
        weatherRv = findViewById(R.id.Weather);
        backIV = findViewById(R.id.IVBack);
        iconIV = findViewById(R.id.Icon);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this,weatherRVModalArrayList);
        weatherRv.setAdapter(weatherRVAdapter);

        Intent intent = getIntent();

        String cityName = intent.getStringExtra("cityName");

        System.out.println(cityName);



        getWeatherInfo(cityName);


    }



    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=c56d2c8253084610be3165039242204&q="+ cityName +"&days=1&aqi=yes&alerts=yes";
        Log.d("WeatherActivity", "Request URL: " + url); // Add this line to log the formed URL
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("WeatherActivity", "Response received: " + response.toString()); // Add this line to log the response
                loadingPB.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature+"°c");

                    System.out.println(response);
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);

                    if(isDay == 1){
                        Picasso.get().load("https://i.pinimg.com/564x/45/ef/8a/45ef8add5371038f4e4a3c82ab4744ab.jpg").into(backIV);
                    }
                    else{
                        Picasso.get().load("https://i.pinimg.com/564x/db/14/c7/db14c786d16e7aa485b70105124bd58a.jpg").into(backIV);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO =  forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");

                    for (int i=0; i<hourArray.length();i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time =  hourObj.getString("time");
                        String temper =  hourObj.getString("temp_c");
                        String img =  hourObj.getJSONObject("condition").getString("icon");
                        String wind =  hourObj.getString("wind_kph");
                        weatherRVModalArrayList.add(new WeatherRVModal(time,temper,img,wind));

                    }
                    weatherRVAdapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("WeatherActivity", "Error: " + volleyError.toString()); // Log the error
                Toast.makeText(WeatherActivity.this,"Please Give a Valid City Name",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
