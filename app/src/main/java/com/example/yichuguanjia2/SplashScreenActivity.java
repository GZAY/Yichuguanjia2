package com.example.yichuguanjia2;
/**
 * 描述：启动界面
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.yichuguanjia2.Login.LoginService;

public class SplashScreenActivity extends Activity {
    /**
     * 延迟时间
     */
    private static final int DELAY_TIME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 利用消息处理器实现延迟跳转到登录窗口
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 启动登录窗口
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                // 关闭启动画面
                finish();
            }
        }, DELAY_TIME);
    }
}
