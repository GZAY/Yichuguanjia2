package com.example.yichuguanjia2.w_Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.adapter.ItemAdapter;
import com.example.yichuguanjia2.base.listItem;

import java.util.ArrayList;
import java.util.List;

public class w_type extends Activity implements AdapterView.OnItemClickListener {
    int[] drawable_image = {R.drawable.top,R.drawable.shirt,R.drawable.coat,R.drawable.trouser,R.drawable.skirt,R.drawable.shoes,R.drawable.bag};
    String[] type = {"上衣","衬衫","外套","裤子","裙子","鞋","包"};
    private List<listItem> browsers = new ArrayList<>();
    ListView listView;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_season);
        back = findViewById(R.id.type_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化数据
        for(int i = 0; i < drawable_image.length;i++){
            listItem it = new listItem(drawable_image[i],type[i],R.drawable.right_black);
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
        Intent intent = new Intent(this,My_case.class);
        intent.putExtra("content", type[position]);
        intent.putExtra("pos", getIntent().getIntExtra("pos", 0));
        startActivityForResult(intent,110);
    }
}
