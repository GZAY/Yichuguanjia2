package com.example.yichuguanjia2.base;

public class Image {
    private String title; //标题
    private String imageUrl; //海报

    public Image(String title, String imageUrl, String time, String average, String genres) {
        this.title = title;
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
