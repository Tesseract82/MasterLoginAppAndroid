package com.example.unit271.loginmasterapp;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firebase.client.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by unit271 on 8/5/16.
 */
public class personObject {

    @JsonProperty("CurrentlySignedInCompetition")
    private boolean currentlySignedInCompetition;
    @JsonProperty("CurrentlySignedInRobotics")
    private boolean currentlySignedInRobotics;
    @JsonProperty("CurrentlySignedInFM")
    private boolean currentlySignedInFM;
    @JsonProperty("SubtractHoursCompetition")
    private int subtractHoursCompetition;
    @JsonProperty("SubtractHoursRobotics")
    private int subtractHoursRobotics;
    @JsonProperty("SubtractHoursFM")
    private int subtractHoursFM;
    @JsonProperty("Logins")
    private LinkedHashMap<String, LoginOutObject> logins;
    @JsonProperty("Password")
    private String Password;
    @JsonIgnore
    private String name;
    @JsonIgnore
    private int totalTime;

    public personObject(){

    }

    public void setcurrentlySignedInCompetition(boolean currentlySignedInCompetition){
        this.currentlySignedInCompetition = currentlySignedInCompetition;
    }

    public void setcurrentlySignedInRobotics(boolean currentlySignedInRobotics){
        this.currentlySignedInRobotics = currentlySignedInRobotics;
    }

    public void setcurrentlySignedInFM(boolean currentlySignedInFM){
        this.currentlySignedInFM = currentlySignedInFM;
    }

    public void setsubtractHoursCompetition(int subtractHoursCompetition){
        this.subtractHoursCompetition = subtractHoursCompetition;
    }

    public void setsubtractHoursRobotics(int subtractHoursRobotics){
        this.subtractHoursRobotics = subtractHoursRobotics;
    }

    public void setsubtractHoursFM(int subtractHoursFM){
        this.subtractHoursFM = subtractHoursFM;
    }

    public void setlogins(LinkedHashMap<String, LoginOutObject> logins){
        this.logins = new LinkedHashMap<String, LoginOutObject>();
        this.logins.putAll(logins);
    }

    public void setPersonName(String name){
        this.name = name;
    }

    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }

    public void setPassword(String Password){
        this.Password = Password;
    }

    //**********************************************************************************************

    public boolean getcurrentlySignedInCompetition(){
        return currentlySignedInCompetition;
    }

    public boolean getcurrentlySignedInRobotics(){
        return currentlySignedInRobotics;
    }

    public boolean getcurrentlySignedInFM(){
        return currentlySignedInFM;
    }

    public int getsubtractHoursCompetition(){
        return subtractHoursCompetition;
    }

    public int getsubtractHoursRobotics(){
        return subtractHoursRobotics;
    }

    public int getsubtractHoursFM(){
        return subtractHoursFM;
    }

    public LinkedHashMap<String, LoginOutObject> getlogins(){
        return logins;
    }

    public String getPersonName(){
        return name;
    }

    public int getTotalTime(){
        return totalTime;
    }

    public String getPassword(){
        return Password;
    }
}
