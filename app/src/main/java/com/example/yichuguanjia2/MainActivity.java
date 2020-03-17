package com.example.yichuguanjia2;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yichuguanjia2.BottomNavigatioinBar.CollocationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.InspirationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.MineFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.WardrobeFragment;
import com.example.yichuguanjia2.base.BaseActivity;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RadioGroup mRgBottomMenu;
    private Fragment[] mFragments;  //数组 存储Fragment
    private int currentIndex;   //当前Fragment的下标

    //使用数组存放图片ID
    private int[] res = {
            R.id.button_camera, R.id.button_album, R.id.button_web};
    //使用List存放图片
    private List<ImageView> imgs = new ArrayList<ImageView>();
    //按钮点击事件的标志
    private boolean flag = true;
    //菜单按钮
    private ImageView imageviewstart;

    //菜单点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (flag) {
                    flag = false;
                    //十字架旋转动画
                    RotateAnimation animation = new RotateAnimation(0.0f, 45.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(150);
                    animation.setFillAfter(true);
                    imageviewstart.startAnimation(animation);
                    startAnim();//子菜单弹出动画
                } else {
                    flag = true;
                    RotateAnimation animation = new RotateAnimation(45.0f, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(150);
                    animation.setFillAfter(true);
                    imageviewstart.startAnimation(animation);
                    closeAnim();//子菜单收回动画
                }
                break;
            case R.id.button_camera:
                ImageView camera = findViewById(R.id.button_camera);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //动态权限：点击相机时获取相机权限
                        DongTaiShare();
                        //从相机获取图片
                        getPicFromCamera();
                    }
                });
                break;
            case R.id.button_album:
                ImageView album = findViewById(R.id.button_album);
                album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册获取图片
                        getPicFromAlbm();
                    }
                });
                break;
            case R.id.button_web:
                break;
            default:
                Toast.makeText(MainActivity.this, "Click" + v.getId(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //打开菜单动画
    private void startAnim() {
        float myroate = (float) (Math.PI / 2);
        for (int i = 0; i < res.length; i++) {
            float x = (float) (200 * Math.cos(myroate * i)); //左右的横坐标
            float y = (float) (120 * Math.sin(myroate * i)); //中间菜单的纵坐标

            //使用属性动画的平移动画，将坐标从原点移动到每个子菜单对应的位置
            ObjectAnimator animator = ObjectAnimator.ofFloat(imgs.get(i),
                    "translationY", 0, -y-250); //250是左右的纵坐标
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(imgs.get(i),
                    "translationX", 0, -x);
            AnimatorSet set = new AnimatorSet();
            animator.setInterpolator(new BounceInterpolator());
            animator2.setInterpolator(new BounceInterpolator());
            set.playTogether(animator, animator2);
            set.setDuration(1000);
            set.start();
        }
    }

    //关闭菜单动画
    private void closeAnim() {
        //一共是3个子菜单，3个子菜单就是把90度分成3分，然后以这个为基准计算菜单的弧度。PI/2/3
        float myroate = (float) (Math.PI / 2);
        for (int i = 0; i < res.length; i++) {
            //500是半径，通过三角函数计算坐标
            float x = (float) (200 * Math.cos(myroate * i));
            float y = (float) (120 * Math.sin(myroate * i));

            //使用属性动画的平移动画，将坐标从现在的位置移回到原点
            ObjectAnimator animator = ObjectAnimator.ofFloat(imgs.get(i),
                    "translationY", -y-250, 0);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(imgs.get(i),
                    "translationX", -x, 0);
            //使用AnimatorSet可以同时播放多个属性动画
            AnimatorSet set = new AnimatorSet();
            //使用自由落体的差值器
            animator.setInterpolator(new BounceInterpolator());
            animator2.setInterpolator(new BounceInterpolator());
            //设定同时播放
            set.playTogether(animator, animator2);
            //设定播放时间
            set.setDuration(1000);
            //开始播放动画（千万不要忘记这一行）
            set.start();
        }
    }

    //添加动态权限
    private void DongTaiShare() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 123);
        }
    }

    //调用系统相机
    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //调用相册
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            // 调用相机后返回
            case 1:
                if (resultCode == RESULT_OK) {
                    final Bitmap photo = intent.getParcelableExtra("data");


                }
                break;
            //调用相册后返回
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);//裁剪图片
                }
                break;
            //调用剪裁后返回
            case 3:
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //设置到ImageView上
                    // mIcon.setImageBitmap(image);
                    /*
                    //也可以进行一些保存、压缩等操作后上传
                    String path = saveImage("userHeader", image);
                    File file = new File(path);

                     //这里可以做上传文件的额操作

                    */
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //导航栏中间菜单
        imageviewstart = findViewById(R.id.btn_cancel);
        //将7个子菜单图片添加点击事件并添加到List
        for (int i = 0; i < res.length; i++) {
            ImageView imageview = (ImageView) findViewById(res[i]);
            imageview.setOnClickListener(this);
            imgs.add(imageview);
        }
        //主按钮添加点击事件
        imageviewstart.setOnClickListener(this);


    }


    //Fragment启动方法
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


}
