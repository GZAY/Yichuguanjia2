package com.example.yichuguanjia2.M_Activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseActivity;

public class QuestionFeedBack extends BaseActivity {
    Button button;
    EditText editText;
    ImageView back;
    @Override
    protected int attachLayoutRes() {
        return R.layout.m_feedbuck;
    }

    @Override
    protected void initViews() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                Toast.makeText(QuestionFeedBack.this, "已成功提交", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initFindViewById() {
        button = findViewById(R.id.button4);
        editText = findViewById(R.id.hand);
        back = findViewById(R.id.qf_back);
    }
}
