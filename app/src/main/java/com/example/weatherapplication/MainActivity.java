package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weatherapplication.weather.WeatherActivity;

public class MainActivity extends AppCompatActivity {
    EditText city;
    EditText country;
    Button btn;
    public static final int JSON_LOADER=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wireUpListeners();
        if(savedInstanceState!=null)
        {
            city.setText(savedInstanceState.getString("city"));
            country.setText(savedInstanceState.getString("country"));
        }

    }

    private void wireUpListeners() {
        city=(EditText)findViewById(R.id.city);
        country=(EditText)findViewById(R.id.country);
        btn=(Button)findViewById(R.id.getUpdate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateText() && checkConnectivity())
                    getWeatherData();
            }
        });
    }

    //specify the country and city to fetch weather data
    private void getWeatherData() {
        Intent intent=new Intent(this,WeatherActivity.class);
        intent.putExtra("city",city.getText().toString());
        intent.putExtra("country",country.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city.getText().toString());
        outState.putString("country",country.getText().toString());
    }

    //check network availablity and connected
    private boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    //validate the input
    private boolean validateText() {
        if(city==null || city.getText().length()==0||city.getText().toString().trim().length()==0)
        {
            city.setError("please enter a city");
            return false;
        }
        if(country==null || country.getText().toString().length()==0 || country.getText().toString().trim().length()==0)
        {
            country.setError("please enter a country");
            return false;
        }
        return true;
    }

}
