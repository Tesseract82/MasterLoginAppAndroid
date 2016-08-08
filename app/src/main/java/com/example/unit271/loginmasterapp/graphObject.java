package com.example.unit271.loginmasterapp;

/**
 * Created by unit271 on 7/20/16.
 */
public class graphObject {
    int hours;
    String startDate, endDate;
    String year;

    public void setHours(int hours){
        this.hours = hours;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public void setYear(String year){
        this.year = year;
    }

    //****************************************************************

    public int getHours(){
        return hours;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getYear(){
        return year;
    }
}
