package com.example.yichuguanjia2.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public String status;

    public Basic basic;

    public Now now;

    public Suggestion suggestion;

    public Update update;

    @SerializedName("lifestyle")
    public List<lifestyle> comfort;

}
