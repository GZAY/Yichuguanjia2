package com.example.yichuguanjia2.M_Activity;

import android.view.View;
import android.widget.ImageView;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseActivity;

public class M_set extends BaseActivity {
    ImageView back;
    @Override
    protected int attachLayoutRes() {
        return R.layout.m_set;
    }

    @Override
    protected void initViews() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initFindViewById() {
        back = findViewById(R.id.m_set_back);
    }
}
