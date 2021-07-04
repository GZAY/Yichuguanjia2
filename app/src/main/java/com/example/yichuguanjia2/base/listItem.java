package com.example.yichuguanjia2.base;

import android.widget.ImageView;
import android.widget.TextView;

public class listItem {
    int imageView;
    String textView;
    int back;

    public listItem(int imageView, String textView,int back) {
        this.imageView = imageView;
        this.textView = textView;
        this.back = back;
    }
    public int getImageView() {
        return imageView;
    }

    public String getTextView() {
        return textView;
    }

    public int getBack() {
        return back;
    }
}
