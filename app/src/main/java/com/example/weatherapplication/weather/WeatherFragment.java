package com.example.weatherapplication.weather;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weatherapplication.DataModel.Weather;
import com.example.weatherapplication.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
//the fragment displays seven days weather data from current day
//current day's weather information is displayed completely
//the class uses weather task a subclass of AsyncTask to get Weather info
public class WeatherFragment extends Fragment implements WeatherTaskCompleted{


    public WeatherFragment() {
        // Required empty public constructor
    }
    public static final int WEATHER_LOADER=1;
    String city;
    String country;
    List<Weather> w;
    View relative;
    TextView min;
    TextView max;
    TextView des;
    TextView temp;
    TextView wind;
    TextView date;
    TextView cityName;
    TextView pressure;
    TextView errortext;
    ImageView img;
    TextView precipitation;
    RecyclerView recycler;
    WeatherAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weather, container, false);
        wireUpListeners(view);
        return view;
    }

    private void wireUpListeners(View view) {
        img=(ImageView)view.findViewById(R.id.img);
        min=(TextView)view.findViewById(R.id.min);
        max=(TextView)view.findViewById(R.id.max);
        des=(TextView)view.findViewById(R.id.des);
        wind=(TextView)view.findViewById(R.id.wnd);
        date=(TextView)view.findViewById(R.id.date);
        cityName=(TextView)view.findViewById(R.id.city);
        pressure=(TextView)view.findViewById(R.id.pressure);
        precipitation=(TextView)view.findViewById(R.id.humidity);
        temp=(TextView)view.findViewById(R.id.temp);
        errortext=(TextView)view.findViewById(R.id.error);
        relative=view.findViewById(R.id.relative);

        //adapter to display seven days weather data
        recycler=(RecyclerView)view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        adapter=new WeatherAdapter(w);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState==null) {
            Bundle args=this.getArguments();
            city=args.getString("city");
            country=args.getString("country");
            new WeatherTask(city, country,this).execute();
        }
        else {
            fillViewsWithData();
        }
    }


    private void fillViewsWithData() {
            if(w!=null && w.get(0)!=null && w.size()>0)
            {
                Weather curr=w.get(0);
                cityName.setText(curr.getCity());
                int res=getActivity().getResources().getIdentifier(w.get(0).getIcon(),"drawable",getActivity().getPackageName());
                img.setImageResource(res);
                min.setText("min:"+curr.getTempMin()+"°c");
                max.setText("max:"+curr.getTempMax()+"°c");
                precipitation.setText("precipitation:\n"+curr.getPrecp()+"mm");
                des.setText(curr.getDes());
                pressure.setText("pressure:\n"+curr.getPres()+"mb");
                Calendar cl=Calendar.getInstance();
                date.setText(cl.getTime()+"");
                wind.setText("Winddir:\n"+curr.getWindDirection()+"\nWindSpeed:\n"+curr.getWindSpeed()+"m/s");
                temp.setText(curr.getTemp()+"°c");
                adapter.list=w;
                adapter.notifyDataSetChanged();
                return;
            }
            displayError();
    }

    private void displayError() {
        errortext.setVisibility(View.VISIBLE);
        relative.setVisibility(View.GONE);
    }

    //on getting weather details we fill the widgets with data
    @Override
    public void setWeather(List<Weather> weather) {
        w=weather;
        fillViewsWithData();

    }


}
