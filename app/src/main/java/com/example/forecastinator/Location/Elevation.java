package com.example.forecastinator.Location;

public class Elevation
{
    private Metric Metric;

    private Imperial Imperial;

    public void setMetric(Metric Metric){
        this.Metric = Metric;
    }
    public Metric getMetric(){
        return this.Metric;
    }
    public void setImperial(Imperial Imperial){
        this.Imperial = Imperial;
    }
    public Imperial getImperial(){
        return this.Imperial;
    }
}