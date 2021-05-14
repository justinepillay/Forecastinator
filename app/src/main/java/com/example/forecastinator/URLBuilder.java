package com.example.forecastinator;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class URLBuilder
{

    private static  String weatherbaseUrl ="https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    private static String locationbaseURL = "https://dataservice.accuweather.com/locations/v1/cities/search";

    private static final String PARAM_METRIC ="metric";
    private static final String METRIC_VALUE = "true";
    private static final String PARAM_API_KEY = "apikey";
    private static final String PARAM_LONGITUDE= "longitude";
    private static final String PARAM_LATITUDE="latitude";
    private static final String LOGGING_TAG = "URLWECREATED";
    private static final String PARAM_Q = "q";

    public URLBuilder()
    {

    }

    //returns the URL to request for city details when a city name is entered
    public static URL citySearchUrl(String city)
    {

    Uri buildUri = Uri.parse(locationbaseURL).buildUpon().appendQueryParameter(PARAM_API_KEY,BuildConfig.ACCUWEATHER_API_KEY)
            .appendQueryParameter(PARAM_METRIC,
                    METRIC_VALUE).appendQueryParameter(PARAM_Q,
                    city).build();

    URL url = null;
    try
    {
        url = new URL(buildUri.toString());
    } catch (MalformedURLException e)
    {
        e.printStackTrace();
    }

    return url;
}

    //returns the URL to request for city details for a specific location
    public static URL getCityFromLocationURL(String coordinates)
    {

        Uri buildUri = Uri.parse(locationbaseURL).buildUpon().appendQueryParameter(PARAM_Q,
               coordinates)
                .appendQueryParameter(PARAM_API_KEY,
                        BuildConfig.ACCUWEATHER_API_KEY) // passing in api key
                .appendQueryParameter(PARAM_METRIC,
                        METRIC_VALUE).appendQueryParameter(PARAM_Q,
                        coordinates) // passing in metric as measurement unit
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }


    //returns the URL to request for forecasts for a city based on the city Id
    public static URL getForecastURL(String cityId)
    {
        weatherbaseUrl ="https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
        weatherbaseUrl = weatherbaseUrl+cityId;

        Uri buildUri = Uri.parse(weatherbaseUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY,
                        BuildConfig.ACCUWEATHER_API_KEY) // passing in api key
                .appendQueryParameter(PARAM_METRIC,
                        METRIC_VALUE) // passing in metric as measurement unit
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }


}
