package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseFragment;
import com.example.yichuguanjia2.base.MyDialog;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class MineFragment extends Fragment {
    CircleImageView mIcon;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine, container, false);
        mIcon = view.findViewById(R.id.header_image);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog, null);

                final MyDialog mMyDialog = new MyDialog(getActivity(), 0, 0, view, R.style.DialogTheme);
                mMyDialog.setCancelable(true);
                mMyDialog.show();

                Button camera = view.findViewById(R.id.camera);
                Button photo = view.findViewById(R.id.photo);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //动态权限：点击相机时获取相机权限
                        DongTaiShare();
                        //从相机获取图片
                        getPicFromCamera();
                        mMyDialog.dismiss();
                    }
                });
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册获取图片
                        openAlbum();
                        mMyDialog.dismiss();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            // 调用相机后返回
            case 1:
                if (resultCode == RESULT_OK) {
                    final Bitmap photo = intent.getParcelableExtra("data");
                    //给头像设置你相机拍的照片
                    mIcon.setImageBitmap(photo);
                }
                break;

            //调用相册后返回
            case 2:
                //用户操作完成，结果码返回是-1，即RESULT_OK
                if (resultCode == RESULT_OK) {
                    //获取选中文件的定位符
                    Uri uri = intent.getData();
                    Log.e("uri", uri.toString());
                    //使用content的接口
                    ContentResolver cr = getContext().getContentResolver();
                    try {
                        //获取图片
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        mIcon.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                } else {
                    //操作错误或没有选择图片
                    Log.i("MainActivtiy", "operation error");
                }
                break;

            //调用剪裁后返回
            case 3:
                Bundle bundle = intent.getExtras();
                /*if(null!=bundle){
                    Log.i("bb","isNull:"+(null==bundle));
                    Bitmap image = bundle.getParcelable("data");
                    mIcon.setImageBitmap(image);
                }else {
                    Log.i("bb","isNull:"+(null==bundle));
                    Uri uri = intent.getData();
                    if (uri != null) {
                        Bitmap image = BitmapFactory.decodeFile(uri.getPath());
                        mIcon.setImageBitmap(image);
                    }
                }*/
                if (bundle != null) {
                    Bitmap image = bundle.getParcelable("data");
                }
                break;
        }
    }
    //添加动态权限
    private void DongTaiShare() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
        }
    }

    //调用系统相机
    private void getPicFromCamera() {
        //隐式调用照相机程序
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //打开相册
    private void openAlbum() {
        if (Build.VERSION.SDK_INT >= 23) {

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);  //打开相册
        }
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

    //保存图片到本地
    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

