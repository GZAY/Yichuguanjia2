package com.example.yichuguanjia2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yichuguanjia2.BottomNavigatioinBar.CollocationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.InspirationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.MineFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.WardrobeFragment;
import com.example.yichuguanjia2.base.BaseActivity;


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
        //fragment天气信息

    }

    //Fragment启动方法：
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private void replaceFragment(Fragment fragment) {
        // 1.获取FragmentManager，在活动中可以直接通过调用getFragmentManager()方法得到
        fragmentManager =getSupportFragmentManager();
        // 2.开启一个事务，通过调用beginTransaction()方法开启
        transaction = fragmentManager.beginTransaction();
        // 3.向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例
        transaction.replace(R.id.fragment_container, fragment);  //fr_container不能为fragment布局，可使用线性布局相对布局等。
        // 4.使用addToBackStack()方法，将事务添加到返回栈中，填入的是用于描述返回栈的一个名字
        transaction.addToBackStack(null);
        // 5.提交事物,调用commit()方法来完成
        transaction.commit();
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

    private String data;
    public void setData(String data){
        this.data = data;
    }
    public String getData(){
        return data;
    }

}
