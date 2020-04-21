package com.example.yichuguanjia2.weather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

}
