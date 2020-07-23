package com.example.weatherapplication.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.weatherapplication.R;


//it uses a fragment
public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Fragment ft=(Fragment)getSupportFragmentManager().findFragmentById(R.id.relative);
        if(ft==null)
        {
            Bundle args=new Bundle();
            args.putString("city",getIntent().getStringExtra("city"));
            args.putString("country",getIntent().getStringExtra("country"));
            ft=new WeatherFragment();
            ft.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.relative,ft).commit();
        }
    }

}
