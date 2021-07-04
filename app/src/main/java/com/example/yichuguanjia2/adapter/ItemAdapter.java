package com.example.yichuguanjia2.adapter;

import android.content.Context;
import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.listItem;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<listItem> {

    //resourceID指定ListView的布局方式
    private int resourceID;
    //重写BrowserAdapter的构造器
    public ItemAdapter(Context context, int textViewResourceID , List<listItem> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }
    //自定义item资源的解析方式
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前Browser实例
        listItem listItem = getItem(position);
        //使用LayoutInfater为子项加载传入的布局
        View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
        ImageView imageView = view.findViewById(R.id.item_image);
        TextView textView = view.findViewById(R.id.item_text);
        ImageView back = view.findViewById(R.id.item_back);

        //引入Browser对象的属性值
        imageView.setImageResource(listItem.getImageView());
        textView.setText(listItem.getTextView());
        back.setImageResource(listItem.getBack());
        return view;
    }
}
