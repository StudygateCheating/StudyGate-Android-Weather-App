package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public JSONArray timelines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        //viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        autoResponse();
    }


    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void autoResponse() {
        startProgressBar();
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlIpinfo = "https://ipinfo.io/?token=65c38dc00ac738";
        JsonObjectRequest ipinfoRequest = new JsonObjectRequest(Request.Method.GET, urlIpinfo, null,
                ipinfoResponse -> {
                    String loc = "";
                    String city = "";
                    String region = "";
                    String country = "";
                    try {
                        loc = ipinfoResponse.getString("loc");
                        city = ipinfoResponse.getString("city").replaceAll(" ", "+");
                        region = ipinfoResponse.getString("region");
                        country = ipinfoResponse.getString("country");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String urlTomorrowAuto = "http://csci571hw8.us-west-1.elasticbeanstalk.com/api/products/tomorrow?checkbox=true&loc=" + loc + "&autocity=" + city + "&region=" + region + "&country=" + country;
                    JsonObjectRequest tomorrowAutoRequest = new JsonObjectRequest(Request.Method.GET, urlTomorrowAuto, null,
                            tomorrowAutoResponse -> {
                                String temperature = "";
                                String windSpeed = "";
                                String humidity = "";
                                String pressure = "";
                                String visibility = "";
                                String weatherCode = "";
                                try {
                                    TextView autoCity = (TextView) findViewById(R.id.city);
                                    autoCity.setText(tomorrowAutoResponse.getString("title"));
                                    timelines = tomorrowAutoResponse.getJSONObject("result").getJSONObject("data").getJSONArray("timelines");
                                    JSONObject values =timelines.getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
                                    temperature = Math.round(Double.parseDouble(values.getString("temperature"))) + "°F";
                                    windSpeed = values.getString("windSpeed") + "mph";
                                    humidity = values.getString("humidity") + "%";
                                    pressure = values.getString("pressureSeaLevel") + "inHg";
                                    visibility = values.getString("visibility") + "mi";
                                    weatherCode = values.getString("weatherCode");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ImageView currentWeather = (ImageView) findViewById(R.id.current_weather);
                                currentWeather.setImageResource(urlCode(weatherCode));
                                TextView currentTemperature = (TextView) findViewById(R.id.current_temperature);
                                currentTemperature.setText(temperature);
                                TextView currentStatus = (TextView) findViewById(R.id.current_status);
                                currentStatus.setText(descriptCode(weatherCode));
                                TextView currentHumidity = (TextView) findViewById(R.id.humidity_data);
                                currentHumidity.setText(humidity);
                                TextView currentWind = (TextView)findViewById(R.id.wind_data);
                                currentWind.setText(windSpeed);
                                TextView currentVisibility = (TextView)findViewById(R.id.visibility_data);
                                currentVisibility.setText(visibility);
                                TextView currentPressure = (TextView)findViewById(R.id.pressure_data);
                                currentPressure.setText(pressure);
                                for (int i = 0; i < 7; i++) {
                                    String date = "";
                                    String statusCode = "";
                                    String tempLow = "";
                                    String tempHi = "";
                                    try {
                                        JSONArray intervals = timelines.getJSONObject(1).getJSONArray("intervals");
                                        date = intervals.getJSONObject(i).getString("startTime").split("T")[0];
                                        statusCode = intervals.getJSONObject(i).getJSONObject("values").getString("weatherCode");
                                        tempLow = String.valueOf(Math.round(Double.parseDouble(intervals.getJSONObject(i).getJSONObject("values").getString("temperatureMin"))));
                                        tempHi = String.valueOf(Math.round(Double.parseDouble(intervals.getJSONObject(i).getJSONObject("values").getString("temperatureMax"))));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    TextView tableDate = (TextView) findViewById(getResId("date" + (i + 1), R.id.class));
                                    tableDate.setText(date);
                                    ImageView tableStatus = (ImageView)findViewById(getResId("status" + (i + 1), R.id.class));
                                    tableStatus.setImageResource(urlCode(statusCode));
                                    TextView tableLow = (TextView) findViewById(getResId("temp_low" + (i + 1), R.id.class));
                                    tableLow.setText(tempLow);
                                    TextView tableHi = (TextView) findViewById(getResId("temp_high" + (i + 1), R.id.class));
                                    tableHi.setText(tempHi);

                                    CardView goToDetails = (CardView) findViewById(R.id.card1);
                                    goToDetails.setOnClickListener(v -> {
                                        Intent intent = new Intent(MainActivity.this, Details.class);
                                        intent.putExtra("tomorrowResponse", tomorrowAutoResponse.toString());
                                        startActivity(intent);
                                    });
                                    stopProgressBar();
                                }
                            }, TomorrowAutoError -> Log.e("tomorrow auto error", TomorrowAutoError.toString()));
                    queue.add(tomorrowAutoRequest);

                }, ipinfoError -> Log.e("ipinfo error", ipinfoError.toString()));
        queue.add(ipinfoRequest);

    }

    private void startProgressBar() {
        findViewById(R.id.linear_layout_center).setVisibility(View.GONE);
        findViewById(R.id.current_weather).setVisibility(View.GONE);
        findViewById(R.id.lower_card_view).setVisibility(View.GONE);
        findViewById(R.id.card1).setBackgroundColor(Color.rgb(48,44,44));
        findViewById(R.id.card_2).setBackgroundColor(Color.rgb(48,44,44));
    }

    private void stopProgressBar() {
        findViewById(R.id.linear_layout_center).setVisibility(View.VISIBLE);
        findViewById(R.id.current_weather).setVisibility(View.VISIBLE);
        findViewById(R.id.lower_card_view).setVisibility(View.VISIBLE);
        findViewById(R.id.pbr).setVisibility(View.GONE);
        findViewById(R.id.progress_text).setVisibility(View.GONE);
        findViewById(R.id.card1).setBackgroundColor(Color.rgb(40,36,36));
        findViewById(R.id.card_2).setBackgroundColor(Color.rgb(40,36,36));
    }

    private int urlCode(String code) {
        int url = R.drawable.clear_day;
        if (code.equals("4201")) {
            url = R.drawable.rain_heavy;
        } else if (code.equals("4001")) {
            url = R.drawable.rain;
        } else if (code.equals("4200")) {
            url = R.drawable.rain_light;
        } else if (code.equals("6201")) {
            url = R.drawable.freezing_rain_heavy;
        } else if (code.equals("6001")) {
            url = R.drawable.freezing_rain;
        } else if (code.equals("6200")) {
            url = R.drawable.freezing_rain_light;
        } else if (code.equals("6000")) {
            url = R.drawable.freezing_drizzle;
        } else if (code.equals("4000")) {
            url = R.drawable.drizzle;
        } else if (code.equals("7101")) {
            url = R.drawable.ice_pellets_heavy;
        } else if (code.equals("7000")) {
            url = R.drawable.ice_pellets;
        } else if (code.equals("7102")) {
            url = R.drawable.ice_pellets_light;
        } else if (code.equals("5101")) {
            url = R.drawable.snow_heavy;
        } else if (code.equals("5000")) {
            url = R.drawable.snow;
        } else if (code.equals("5100")) {
            url = R.drawable.snow_light;
        } else if (code.equals("5001")) {
            url = R.drawable.flurries;
        } else if (code.equals("8000")) {
            url = R.drawable.tstorm;
        } else if (code.equals("2100")) {
            url = R.drawable.fog_light;
        } else if (code.equals("2000")) {
            url = R.drawable.fog;
        } else if (code.equals("1001")) {
            url = R.drawable.cloudy;
        } else if (code.equals("1102")) {
            url = R.drawable.mostly_cloudy;
        } else if (code.equals("1101")) {
            url = R.drawable.partly_cloudy_day;
        } else if (code.equals("1100")) {
            url = R.drawable.mostly_clear_day;
        } else if (code.equals("1000")) {
            url = R.drawable.clear_day;
        } else if (code.equals("3000")) {
            url = R.drawable.light_wind;
        } else if (code.equals("3001")) {
            url = R.drawable.wind;
        } else if (code.equals("3002")) {
            url = R.drawable.strong_wind;
        }
        return url;
    }

    private String descriptCode(String code) {
        String descript = "";
        if (code.equals("4201")) {
            descript = "Heavy Rain";
        } else if (code.equals("4001")) {
            descript = "Rain";
        } else if (code.equals("4200")) {
            descript = "Light Rain";
        } else if (code.equals("6201")) {
            descript = "Heavy Freezing Rain";
        } else if (code.equals("6001")) {
            descript = "Freezing Rain";
        } else if (code.equals("6200")) {
            descript = "Light Freezing Rain";
        } else if (code.equals("6000")) {
            descript = "Freezing Drizzle";
        } else if (code.equals("4000")) {
            descript = "Drizzle";
        } else if (code.equals("7101")) {
            descript = "Heavy Ice Pellets";
        } else if (code.equals("7000")) {
            descript = "Ice Pellets";
        } else if (code.equals("7102")) {
            descript = "Light Ice Pellets";
        } else if (code.equals("5101")) {
            descript = "Heavy Snow";
        } else if (code.equals("5000")) {
            descript = "Snow";
        } else if (code.equals("5100")) {
            descript = "Light Snow";
        } else if (code.equals("5001")) {
            descript = "Flurries";
        } else if (code.equals("8000")) {
            descript = "Thunderstorm";
        } else if (code.equals("2100")) {
            descript = "Light Fog";
        } else if (code.equals("2000")) {
            descript = "Fog";
        } else if (code.equals("1001")) {
            descript = "Cloudy";
        } else if (code.equals("1102")) {
            descript = "Mostly Cloudy";
        } else if (code.equals("1101")) {
            descript = "Partly Cloudy";
        } else if (code.equals("1100")) {
            descript = "Mostly Clear";
        } else if (code.equals("1000")) {
            descript = "Clear";
        } else if (code.equals("3000")) {
            descript = "Light Wind";
        } else if (code.equals("3001")) {
            descript = "Wind";
        } else if (code.equals("3002")) {
            descript = "Strong Wind";
        }
        return descript;
    }
}
