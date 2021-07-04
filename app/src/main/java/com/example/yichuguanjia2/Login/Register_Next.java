package com.example.yichuguanjia2.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yichuguanjia2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class Register_Next extends AppCompatActivity implements View.OnClickListener {
    private EditText register_username;
    private EditText register_pwd_input;
    private EditText et_register_pwd_input_reInput;
    private Button register_submit;
    ImageButton navigation_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_two);
        register_username = findViewById(R.id.et_register_username);
        register_pwd_input = findViewById(R.id.et_register_pwd_input);
        et_register_pwd_input_reInput = findViewById(R.id.et_register_pwd_input_reInput);
        register_submit = findViewById(R.id.bt_register_submit);
        navigation_back = findViewById(R.id.ib_navigation_back);
        register_submit.setOnClickListener(this);
        navigation_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_submit:
                registerWithOkHttp(register_username.getText().toString(),register_pwd_input.getText().toString(), et_register_pwd_input_reInput.getText().toString());
                break;
            case R.id.ib_navigation_back:
                finish();
                break;
        }
    }
    //实现注册
    private void registerWithOkHttp(String phone, String password, String repassword){
        if(!password.equals(repassword)){
            Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }else if (phone.length()!=11){
            Toast.makeText(this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.length()<6){
            Toast.makeText(this, "密码太短！", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtil.registerWithOkHttp(phone, password, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Register_Next", "注册请求失败");
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //获取返回内容
                final String responseData = response.body().string();
                //在主线程更新ui和响应用户操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("200")){
                            Toast.makeText(getApplicationContext(), "注册成功！请登录", Toast.LENGTH_SHORT).show();
                            Register_Next.this.finish();
                        }else if (responseData.equals("exist")){
                            Toast.makeText(getApplicationContext(), "该手机号已注册！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
