package com.example.forecastinator.Forecast;

public class Minimum
{
    private double Value;

    private String Unit;

    private int UnitType;

    public void setValue(double Value){
        this.Value = Value;
    }
    public double getValue(){
        return this.Value;
    }
    public void setUnit(String Unit){
        this.Unit = Unit;
    }
    public String getUnit(){
        return this.Unit;
    }
    public void setUnitType(int UnitType){
        this.UnitType = UnitType;
    }
    public int getUnitType(){
        return this.UnitType;
    }
}