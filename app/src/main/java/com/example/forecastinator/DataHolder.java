package com.example.forecastinator;

import java.util.ArrayList;

public class DataHolder
{
    public static ArrayList<ForecastItem> forecastItems;


    public DataHolder()
    {

    }

    public static void setList(ArrayList<ForecastItem> items)
    {
        forecastItems = items;
    }

    public static ArrayList<ForecastItem> getList()
    {
        return forecastItems;
    }





}
