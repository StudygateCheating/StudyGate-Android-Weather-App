package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Today#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Today extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject tomorrowResponse;

    public Today() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Today.
     */
    // TODO: Rename and change types and number of parameters
    public static Today newInstance(String param1, String param2) {
        Today fragment = new Today();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_today, container, false);
        if (getArguments() != null) {
            JSONObject values = new JSONObject();
            try {
                tomorrowResponse = new JSONObject(getArguments().getString("tomorrowResponse"));
                JSONArray timelines = tomorrowResponse.getJSONObject("result").getJSONObject("data").getJSONArray("timelines");
                values = timelines.getJSONObject(0).getJSONArray("intervals").getJSONObject(0).getJSONObject("values");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {
                String windData = "";
                String pressureData = "";
                String precipitationData = "";
                String temperatrueData = "";
                String weatherCode = "";
                String humidityData = "";
                String visibilityData = "";
                String cloudData = "";
                String ozoneData = "";
                try {
                    windData = values.getString("windSpeed") + " mph";
                    pressureData = values.getString("pressureSeaLevel") + " inHg";
                    precipitationData = values.getString("precipitationProbability") + " %";
                    temperatrueData = Math.round(Double.parseDouble(values.getString("temperature")))  + "°F";
                    weatherCode = values.getString("weatherCode");
                    humidityData = values.getString("humidity") + "%";
                    visibilityData = values.getString("visibility") + " mi";
                    cloudData = values.getString("cloudCover") + "%";
                    ozoneData = String.valueOf(Math.round(Double.parseDouble(values.getString("uvIndex")) * 100.00) / 100.00);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView windSpeed = (TextView) inf.findViewById(R.id.ws);
                windSpeed.setText(windData);
                TextView pressure = (TextView) inf.findViewById(R.id.ps);
                pressure.setText(pressureData);
                TextView precipitation = (TextView) inf.findViewById(R.id.pc);
                precipitation.setText(precipitationData);
                TextView temperature = (TextView) inf.findViewById(R.id.tp);
                temperature.setText(temperatrueData);
                TextView humidity = (TextView) inf.findViewById(R.id.hm);
                humidity.setText(humidityData);
                TextView visibility = (TextView) inf.findViewById(R.id.vs);
                visibility.setText(visibilityData);
                TextView cloudCover = (TextView) inf.findViewById(R.id.cc);
                cloudCover.setText(cloudData);
                TextView ozone = (TextView) inf.findViewById(R.id.oz);
                ozone.setText(ozoneData);
                ImageView status = (ImageView) inf.findViewById(R.id.st);
                status.setImageResource(urlCode(weatherCode));
                TextView description = (TextView) inf.findViewById(R.id.ds);
                description.setText(descriptCode(weatherCode));
            }
        }
        // Inflate the layout for this fragment
        return inf;
    }

    private int urlCode(String code) {
        int url = R.drawable.clear_day;
        if (code.equals("4201")) {
            url = R.drawable.rain_heavy;
        }
        else if (code.equals("4001")) {
            url = R.drawable.rain;
        }
        else if (code.equals("4200")) {
            url = R.drawable.rain_light;
        }
        else if (code.equals("6201")) {
            url = R.drawable.freezing_rain_heavy;
        }
        else if (code.equals("6001")) {
            url = R.drawable.freezing_rain;
        }
        else if (code.equals("6200")) {
            url = R.drawable.freezing_rain_light;
        }
        else if (code.equals("6000")) {
            url = R.drawable.freezing_drizzle;
        }
        else if (code.equals("4000")) {
            url = R.drawable.drizzle;
        }
        else if (code.equals("7101")) {
            url = R.drawable.ice_pellets_heavy;
        }
        else if (code.equals("7000")) {
            url = R.drawable.ice_pellets;
        }
        else if (code.equals("7102")) {
            url = R.drawable.ice_pellets_light;
        }
        else if (code.equals("5101")) {
            url = R.drawable.snow_heavy;
        }
        else if (code.equals("5000")) {
            url = R.drawable.snow;
        }
        else if (code.equals("5100")) {
            url = R.drawable.snow_light;
        }
        else if (code.equals("5001")) {
            url = R.drawable.flurries;
        }
        else if (code.equals("8000")) {
            url = R.drawable.tstorm;
        }
        else if (code.equals("2100")) {
            url = R.drawable.fog_light;
        }
        else if (code.equals("2000")) {
            url = R.drawable.fog;
        }
        else if (code.equals("1001")) {
            url = R.drawable.cloudy;
        }
        else if (code.equals("1102")) {
            url = R.drawable.mostly_cloudy;
        }
        else if (code.equals("1101")) {
            url = R.drawable.partly_cloudy_day;
        }
        else if (code.equals("1100")) {
            url = R.drawable.mostly_clear_day;
        }
        else if (code.equals("1000")) {
            url = R.drawable.clear_day;
        }
        else if (code.equals("3000")) {
            url = R.drawable.light_wind;
        }
        else if (code.equals("3001")) {
            url = R.drawable.wind;
        }
        else if (code.equals("3002")) {
            url = R.drawable.strong_wind;
        }
        return url;
    }

    private String descriptCode(String code) {
        String descript = "";
        if (code.equals("4201")) {
            descript = "Heavy Rain";
        }
        else if (code.equals("4001")) {
            descript = "Rain";
        }
        else if (code.equals("4200")) {
            descript = "Light Rain";
        }
        else if (code.equals("6201")) {
            descript = "Heavy Freezing Rain";
        }
        else if (code.equals("6001")) {
            descript = "Freezing Rain";
        }
        else if (code.equals("6200")) {
            descript = "Light Freezing Rain";
        }
        else if (code.equals("6000")) {
            descript = "Freezing Drizzle";
        }
        else if (code.equals("4000")) {
            descript = "Drizzle";
        }
        else if (code.equals("7101")) {
            descript = "Heavy Ice Pellets";
        }
        else if (code.equals("7000")) {
            descript = "Ice Pellets";
        }
        else if (code.equals("7102")) {
            descript = "Light Ice Pellets";
        }
        else if (code.equals("5101")) {
            descript = "Heavy Snow";
        }
        else if (code.equals("5000")) {
            descript = "Snow";
        }
        else if (code.equals("5100")) {
            descript = "Light Snow";
        }
        else if (code.equals("5001")) {
            descript = "Flurries";
        }
        else if (code.equals("8000")) {
            descript = "Thunderstorm";
        }
        else if (code.equals("2100")) {
            descript = "Light Fog";
        }
        else if (code.equals("2000")) {
            descript = "Fog";
        }
        else if (code.equals("1001")) {
            descript = "Cloudy";
        }
        else if (code.equals("1102")) {
            descript = "Mostly Cloudy";
        }
        else if (code.equals("1101")) {
            descript = "Partly Cloudy";
        }
        else if (code.equals("1100")) {
            descript = "Mostly Clear";
        }
        else if (code.equals("1000")) {
            descript = "Clear";
        }
        else if (code.equals("3000")) {
            descript = "Light Wind";
        }
        else if (code.equals("3001")) {
            descript = "Wind";
        }
        else if (code.equals("3002")) {
            descript = "Strong Wind";
        }
        return descript;
    }
}