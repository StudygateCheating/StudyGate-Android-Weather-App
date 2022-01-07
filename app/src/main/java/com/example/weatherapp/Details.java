package com.example.weatherapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.ui.main.SectionsPagerAdapter;
import com.example.weatherapp.databinding.ActivityDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Details extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private JSONObject tomorrowResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String responseTitle = "";
        try {
            tomorrowResponse = new JSONObject(getIntent().getStringExtra("tomorrowResponse"));
            responseTitle = tomorrowResponse.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(responseTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = null;
        try {
            sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), tomorrowResponse.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}