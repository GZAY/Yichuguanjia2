package com.example.yichuguanjia2;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yichuguanjia2.BottomNavigatioinBar.CollocationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.InspirationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.MineFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.WardrobeFragment;
import com.example.yichuguanjia2.base.BaseActivity;
import com.example.yichuguanjia2.weather.HttpUtil;
import com.example.yichuguanjia2.weather.Province;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup mRgBottomMenu;
    private Fragment[] mFragments;  //数组 存储Fragment
    private int currentIndex;   //当前Fragment的下标

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    //Fragment,底部导航栏
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }
    @Override
    protected void initFindViewById() {
        mRgBottomMenu = findViewById(R.id.tabs_rg);
    }
    @Override
    protected void initViews() {
        //将Fragment加入数组
        mFragments = new Fragment[] {
                new WardrobeFragment(),
                new CollocationFragment(),
                new InspirationFragment(),
                new MineFragment()
        };
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置为默认界面 MainHomeFragment
        ft.add(R.id.fragment_container,mFragments[0]).commit();
        //RadioGroup选中事件监听 改变fragment
        mRgBottomMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.title_Wardrobe:
                        setIndexSelected(0);
                        break;
                    case R.id.title_collocation:
                        setIndexSelected(1);
                        break;
                    case R.id.title_inspiration:
                        setIndexSelected(2);
                        break;
                    case R.id.title_mine:
                        setIndexSelected(3);
                        break;
                    default:
                        break;
                }
            }
        });
        final Button imageButton = findViewById(R.id.btn_cancel);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final RotateAnimation animation = new RotateAnimation(0.0f, 45.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration( 360 );
                imageButton.startAnimation( animation );
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 0.6f;
//                getWindow().setAttributes(lp);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(MainActivity.this, null);
                //显示窗口
                menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 20, 20); //设置layout在PopupWindow中显示的位置*/
            }
        });
    }
    //设置Fragment页面
    private void setIndexSelected(int index) {
        if (currentIndex == index) {
            return;
        }
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //隐藏当前Fragment
        ft.hide(mFragments[currentIndex]);
        //判断Fragment是否已经添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fragment_container,mFragments[index]).show(mFragments[index]);
        }else {
            //显示新的Fragment
            ft.show(mFragments[index]);
        }
        ft.commit();
        currentIndex = index;
    }

    @Override
    public void onClick(View v) {

    }

}
