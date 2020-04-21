package com.example.yichuguanjia2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.adapter.ItemAdapter;
import com.example.yichuguanjia2.base.listItem;

import java.util.ArrayList;
import java.util.List;

public class w_type extends Activity implements AdapterView.OnItemClickListener {
    int[] drawable_image = {R.drawable.icon_compass,R.drawable.icon_album,R.drawable.icon_photo};
    String[] type = {"上衣","外套","连衣裙","半裙","裤子"};
    private List<listItem> browsers = new ArrayList<>();
    static TextView num;
    static int position;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_season);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        //初始化数据
        for(int i = 0; i < drawable_image.length;i++){
            String n = pref.getString("num"+i,"0");
            listItem it = new listItem(drawable_image[i],type[i],n,R.drawable.right_black);
            browsers.add(it);
        }

        //初始化适配器
        ItemAdapter adapter = new ItemAdapter(w_type.this, R.layout.activity_item, browsers);
        listView = findViewById(R.id.w_season);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,w_clothes.class);
        intent.putExtra("version", position+1);
        intent.putExtra("position", position);
        startActivityForResult(intent,110);
    }
    String n = "";
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 110:
                int number = data.getIntExtra("number",0);
                position = data.getIntExtra("position",0);
                View view = listView.getChildAt(position);
                num =view.findViewById(R.id.item_num);
                n = number +"";
                num.setText(n);
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("num"+position,n);
                editor.apply();
                break;
            default:
        }
    }

}
