package com.example.weatherapplication.weather;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.DataModel.Weather;
import com.example.weatherapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//we make a horizontal recycler view to display seven days weather data
//seven days from current day weather data has min,max,avg and weather description
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
    List<Weather> list;
    String days[];
    public WeatherAdapter(List<Weather> list)
    {
        days=new String[]{"dummy","SUN","MON","TUE","WED","THU","FRI","SAT"};
        this.list=list;
    }
    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.weather_list,parent,false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder holder, int position) {
        Calendar cl=Calendar.getInstance();
        cl.add(Calendar.DATE,position);
        holder.day.setText(days[cl.get(Calendar.DAY_OF_WEEK)]);
        holder.min.setText("min :"+list.get(position).getTempMin()+"°c");
        holder.max.setText("max :"+list.get(position).getTempMax()+"°c");
        holder.temp.setText("avg:"+list.get(position).getTemp()+"°c");
        holder.desc.setText(list.get(position).getDes());
        int res=holder.itemView.getContext().getResources().getIdentifier(list.get(position).getIcon(),"drawable",holder.itemView.getContext().getPackageName());
        holder.img.setImageResource(res);

    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;
        return list.size();
    }

    class WeatherHolder extends RecyclerView.ViewHolder {
        TextView min;
        TextView max;
        TextView temp;
        TextView desc;
        TextView day;
        ImageView img;
        public WeatherHolder(@NonNull View itemView) {
             super(itemView);
             itemView.findViewById(R.id.min);
             min=(TextView)itemView.findViewById(R.id.min);
             max=(TextView)itemView.findViewById(R.id.max);
             desc=(TextView)itemView.findViewById(R.id.des);
             temp=(TextView)itemView.findViewById(R.id.temp);
             day=(TextView)itemView.findViewById(R.id.day);
             img=(ImageView)itemView.findViewById(R.id.img);
         }
     }
}
