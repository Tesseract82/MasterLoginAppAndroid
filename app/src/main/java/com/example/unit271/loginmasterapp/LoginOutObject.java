package com.example.unit271.loginmasterapp;

/**
 * Created by unit271 on 7/19/16.
 */
public class LoginOutObject {
    private String Action, Location, Time;

    public LoginOutObject(){

    }

    public void setAction(String Action){
        this.Action = Action;
    }

    public void setLocation(String Location){
        this.Location = Location;
    }

    public void setTime(String Time){
        this.Time = Time;
    }

    //***************************************************************************

    public String getAction(){
        return Action;
    }

    public String getLocation(){
        return Location;
    }

    public String getTime(){
        return Time;
    }
}
