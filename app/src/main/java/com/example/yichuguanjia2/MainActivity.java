package com.example.yichuguanjia2;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yichuguanjia2.BottomNavigatioinBar.CollocationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.InspirationFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.MineFragment;
import com.example.yichuguanjia2.BottomNavigatioinBar.WardrobeFragment;
import com.example.yichuguanjia2.Login.LoginService;
import com.example.yichuguanjia2.base.BaseActivity;
import com.example.yichuguanjia2.base.listItem;
import com.example.yichuguanjia2.spl_clothes.w_clothes1;
import com.example.yichuguanjia2.sql.MyDatabaseHelper;
import com.example.yichuguanjia2.w_Activity.myClothes;
import com.example.yichuguanjia2.w_Activity.w_type;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RadioGroup mRgBottomMenu;
    private Fragment[] mFragments;  //数组 存储Fragment
    private int currentIndex;   //当前Fragment的下标
    private MyDatabaseHelper myDatabaseHelper;
    //使用数组存放图片ID
    private int[] res = {
            R.id.button_camera, R.id.button_album, R.id.button_web};
    //使用List存放图片
    private List<ImageView> imgs = new ArrayList<ImageView>();
    //按钮点击事件的标志
    private boolean flag = true;
    //菜单按钮
    private ImageView imageviewstart;
    private final int IMAGE_OPEN = 2;        //打开图片标记


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sprfMain= getSharedPreferences("LoginService",MODE_PRIVATE);
        if(!sprfMain.getBoolean("Login",false)) {
            Intent intent = new Intent(this, LoginService.class);
            startActivity(intent);
            finish();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //导航栏中间菜单
        imageviewstart = findViewById(R.id.btn_cancel);
        //主按钮添加点击事件
        imageviewstart.setOnClickListener(this);
        //将子菜单图片添加点击事件并添加到List
        for (int i = 0; i < res.length; i++) {
            ImageView imageview = findViewById(res[i]);
            imageview.setOnClickListener(this);
            imgs.add(imageview);
        }
    }

    //菜单点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_camera:
                ImageView camera = findViewById(R.id.button_camera);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //动态权限：点击相机时获取相机权限
                        DongTaiShare();
                        //从相机获取图片
                        openCamera();
                        closeAnim();
                        RotateAnimation animation = new RotateAnimation(45.0f, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(150);
                        animation.setFillAfter(true);
                        imageviewstart.startAnimation(animation);
                        flag = true;
                    }
                });
                break;
            case R.id.button_album:
                ImageView album = findViewById(R.id.button_album);
                album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册获取图片
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(photoPickerIntent, IMAGE_OPEN);
                        closeAnim();
                        RotateAnimation animation = new RotateAnimation(45.0f, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        animation.setDuration(150);
                        animation.setFillAfter(true);
                        imageviewstart.startAnimation(animation);
                        flag = true;
                    }
                });
                break;
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
            case R.id.button_web:
                break;
            default:
                flag = true;
                RotateAnimation animation = new RotateAnimation(45.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(150);
                animation.setFillAfter(true);
                imageviewstart.startAnimation(animation);
                closeAnim();//子菜单收回动画
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

    private String pathImage;                       //选择图片路径
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
                //打开相机
                if (requestCode == CAMERA_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        if (isAndroidQ) {
                            // Android 10 使用图片uri加载
                            resumeCode = requestCode;
                            final Bitmap photo = getBitmapFromUri(MainActivity.this, mCameraUri);
                            Intent intent1 = new Intent(MainActivity.this, myClothes.class);
                            intent1.putExtra("image",saveBitmap(this,photo));
                            startActivity(intent1);
                        } else {
                            // 使用图片路径加载
                            final Bitmap photo = BitmapFactory.decodeFile(mCameraImagePath);
                            Intent intent1 = new Intent(MainActivity.this, myClothes.class);
                            intent1.putExtra("image",saveBitmap(this,photo));
                            startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            //调用相册后返回
            case 2:
                //打开图片
                if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
                    resumeCode = requestCode;
                    Uri uri = intent.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        //查询选择图片
                        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
                                null,
                                null,
                                null);
                        //返回 没找到选择图片
                        if (null == cursor) {
                            return;
                        }
                        //光标移动至开头 获取图片路径
                        cursor.moveToFirst();
                        pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        final Bitmap bitmap = getBitmapFromUri(MainActivity.this, getImageContentUri(MainActivity.this, pathImage));
                        Intent intent1 = new Intent(MainActivity.this, myClothes.class);
                        intent1.putExtra("image",saveBitmap(this,bitmap));
                        startActivity(intent1);
                    }
                }
                break;
        }
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

    /**
     * 分界线——相机启动的权限申请
     */
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static int resumeCode;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }
    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;
            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }
            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    // 通过uri加载图片
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

}