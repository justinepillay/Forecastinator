package com.example.forecastinator.Location;

public class TimeZone
{
    private String Code;

    private String Name;

    private double GmtOffset;

    private boolean IsDaylightSaving;

    private String NextOffsetChange;

    public void setCode(String Code){
        this.Code = Code;
    }
    public String getCode(){
        return this.Code;
    }
    public void setName(String Name){
        this.Name = Name;
    }
    public String getName(){
        return this.Name;
    }
    public void setGmtOffset(double GmtOffset){
        this.GmtOffset = GmtOffset;
    }
    public double getGmtOffset(){
        return this.GmtOffset;
    }
    public void setIsDaylightSaving(boolean IsDaylightSaving){
        this.IsDaylightSaving = IsDaylightSaving;
    }
    public boolean getIsDaylightSaving(){
        return this.IsDaylightSaving;
    }
    public void setNextOffsetChange(String NextOffsetChange){
        this.NextOffsetChange = NextOffsetChange;
    }
    public String getNextOffsetChange(){
        return this.NextOffsetChange;
    }
}