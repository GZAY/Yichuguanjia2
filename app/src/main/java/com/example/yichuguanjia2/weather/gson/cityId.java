package com.example.yichuguanjia2.weather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class cityId {
    @SerializedName("basic")
    public List<cityInfo_basic> basicsList;
}
