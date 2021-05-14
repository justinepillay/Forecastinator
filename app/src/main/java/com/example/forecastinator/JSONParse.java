package com.example.forecastinator;

import android.util.Log;

import com.example.forecastinator.Forecast.DailyForecasts;
import com.example.forecastinator.Forecast.Root;
import com.example.forecastinator.Location.CityRoot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JSONParse
{

    public JSONParse()
    {

    }

    public ArrayList<ForecastItem> getForecastList(String weatherJSON, String cityName)
    {

        ArrayList<ForecastItem> forecastsLst = new ArrayList<ForecastItem>();

        if (weatherJSON != null)
        {
            Gson gson = new Gson();
            Root weatherData = gson.fromJson(weatherJSON, Root.class);
            List<DailyForecasts> forecasts = weatherData.getDailyForecasts();

            for (int i = 0; i < weatherData.getDailyForecasts().size(); i++)
            {

                forecastsLst.add(new ForecastItem(String.format("https://developer.accuweather.com/sites/default/files/%02d-s.png",forecasts.get(i).getDay().getIcon()), forecasts.get(i).getTemperature().getMaximum().getValue()+"",forecasts.get(i).getTemperature().getMinimum().getValue()+"",forecasts.get(i).getDay().getIconPhrase()+"",forecasts.get(i).getDate().substring(0,10), cityName));

            }
        }

        return forecastsLst;

    }

    public String getFirstCityId(String locationJSON)
    {
        Gson gson = new Gson();
        CityRoot[] locationData = gson.fromJson(locationJSON, CityRoot[].class);

        String firstId = locationData[0].getKey();

        Log.i("CITY_ID", firstId);
        return firstId;

     }

    public String getFirstCityName(String locationJSON)
    {
        Gson gson = new Gson();
        CityRoot[] locationData = gson.fromJson(locationJSON, CityRoot[].class);

        String cityName = locationData[0].getEnglishName();

        Log.i("CITY_ID", cityName);
        return cityName;

    }


    public String getSingleCityId(String locationJSON)
    {
        Gson gson = new Gson();
        CityRoot locationData = gson.fromJson(locationJSON, CityRoot.class);

        String singleId = locationData.getKey();

        return singleId;

    }


    }


