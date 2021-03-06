package com.example.yichuguanjia2.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //返回布局文件ID
        setContentView(attachLayoutRes());
        //找到控件ID
        initFindViewById();
        //初始化控件
        initViews();
    }
    /*protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }*/
    /**
     * 绑定布局文件
     *
     * @return 布局文件的ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 找到控件ID
     */
    protected abstract void initFindViewById();
}