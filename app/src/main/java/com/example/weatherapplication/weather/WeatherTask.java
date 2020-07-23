package com.example.weatherapplication.weather;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.weatherapplication.DataModel.Weather;
import com.example.weatherapplication.OpenWeatherApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.weatherapplication.OpenWeatherApp.API_KEY;
import static com.example.weatherapplication.OpenWeatherApp.AUTH;
import static com.example.weatherapplication.OpenWeatherApp.KEY3;
import static com.example.weatherapplication.OpenWeatherApp.KEY4;
import static com.example.weatherapplication.OpenWeatherApp.PARAM2;
import static com.example.weatherapplication.OpenWeatherApp.PARAM3;
import static com.example.weatherapplication.OpenWeatherApp.PARAM4;
import static com.example.weatherapplication.OpenWeatherApp.SCHEME;
interface WeatherTaskCompleted
{
    void setWeather(List<Weather> weather);
}

//weather task creates a httpconnection to weatherbit api to fetch the json details of a city's weather
//the json result contains seven days weather data
//json results are parsed and added to the weather list
public class WeatherTask extends AsyncTask<Void,Void, List<Weather>>
{
    String city;
    String country;
    WeatherTaskCompleted task;
    WeatherTask(String city,String country,WeatherTaskCompleted task){
        this.task=task;
        this.city=city;
        this.country=country;
    }


    //on postExecute pass the results to the fragment or activity that executed weathertask
    @Override
    protected void onPostExecute(List<Weather> weather) {
        super.onPostExecute(weather);
        task.setWeather(weather);
    }

    @Override
    protected List<Weather> doInBackground(Void... val) {
            String output="";
            BufferedReader reader=null;
            HttpURLConnection http=null;
            try {
                Uri.Builder builder=new Uri.Builder().scheme(SCHEME).authority(AUTH).appendPath(OpenWeatherApp.PATH)
                        .appendQueryParameter(OpenWeatherApp.PARAM1,city+","+country)
                        .appendQueryParameter(PARAM2,API_KEY)
                        .appendQueryParameter(PARAM3,KEY3)
                        .appendQueryParameter(PARAM4,KEY4);
                URL url=new URL(builder.build().toString());
                http=(HttpURLConnection) url.openConnection();
                InputStream input=http.getInputStream();
                reader=new BufferedReader(new InputStreamReader(input));
                String line="";
                while(line!=null) {
                    line=reader.readLine();
                    output += line;

                }
                reader.close();
                http.disconnect();
                //disconnect the connection and parse the JSON data
                return parseJson(output);
            }
            catch (Exception e) {
                Log.d("mytest",e.getMessage());
            }
            return null;
    }
    private List<Weather> parseJson(String output) throws JSONException {
            List<Weather> weatherList=new ArrayList<Weather>();
            JSONObject object=new JSONObject(output);
            int i=1;
            //current day data is parsed completely
            weatherList.add(getWeather(object));

            //for rest of the days fetch only the temperature details and weather desc
            JSONArray array=object.getJSONArray("data");
            while(!array.isNull(i))
            {
                weatherList.add(getConAndTemp((JSONObject) array.get(i)));
                i++;
            }
            return weatherList;
    }

    private Weather getConAndTemp(JSONObject curr) throws JSONException {
        Weather w=new Weather();
        w.setIcon(curr.getJSONObject("weather").getString("icon"));
        w.setDes(curr.getJSONObject("weather").getString("description"));
        w.setTemp(curr.getDouble("temp"));
        w.setTempMin(curr.getDouble("min_temp"));
        w.setTempMax(curr.getDouble("max_temp"));
        return w;
    }

    //for current day weather
    private Weather getWeather(JSONObject object) throws JSONException {

        JSONObject curr=(JSONObject)object.getJSONArray("data").get(0);
        Weather w=getConAndTemp(curr);
        w.setWindSpeed(curr.getDouble("wind_spd"));
        w.setPres(curr.getDouble("pres"));
        w.setWindDirection(curr.getString("wind_cdir_full"));
        w.setPrecp(curr.getDouble("precip"));
        w.setDes(curr.getJSONObject("weather").getString("description"));
        w.setLat(object.getDouble("lat"));
        w.setLon(object.getDouble("lon"));
        w.setCity(object.getString("city_name"));
        return w;
    }


}
