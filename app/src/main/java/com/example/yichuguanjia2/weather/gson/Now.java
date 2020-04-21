package com.example.yichuguanjia2.weather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("fl")
    public String temperature;

    @SerializedName("cond_txt")
    public String info;

    /*@SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;
    }*/

}
