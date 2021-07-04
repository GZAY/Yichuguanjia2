package com.example.yichuguanjia2.M_Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.yichuguanjia2.Login.OkHttpUtil;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.Utils.UI_Tools;
import com.example.yichuguanjia2.base.BaseActivity;
import com.example.yichuguanjia2.base.MyDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class myInfo extends BaseActivity {
    private ImageView back;
    private CircleImageView mIcon;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private LinearLayout cancel;
    private Button updateInfo;
    private String accout;
    @Override
    protected void initFindViewById() {
        back = findViewById(R.id.about_us_back);
        mIcon = findViewById(R.id.header_image);
        editText1 = findViewById(R.id.edit_info1);
        editText2 = findViewById(R.id.edit_info2);
        editText3 = findViewById(R.id.edit_info3);
        editText4 = findViewById(R.id.edit_info4);
        cancel = findViewById(R.id.cancel);
        updateInfo = findViewById(R.id.updateInfo);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.m_aboutus;
    }

    @Override
    protected void initViews() {
        final String password;
        SharedPreferences sprfMain= getSharedPreferences("LoginService",MODE_PRIVATE);
        accout = sprfMain.getString("account", null);
        password = sprfMain.getString("password", null);
        OkHttpUtil.getInfo(accout, password, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("PostInfo","修改失败");
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
                                getJsonData(responseData);
                            }
                        });
                    }
                });
        OkHttpUtil.getpic(accout, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getpic","获取失败");
            }

            //请求成功，获取服务器返回数据
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //获取返回内容
                final byte[] responseData = response.body().bytes();

                //在主线程更新ui和响应用户操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = bytesToImageFile(responseData);
                        mIcon.setImageBitmap(bitmap);
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog, null);
                final MyDialog mMyDialog = new MyDialog(myInfo.this, 0, 0, view, R.style.DialogTheme);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.clearFocus();
            }
        });
        UI_Tools ui_tools = new UI_Tools();
        ui_tools.CancelFocusOne(this, cancel, editText1);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtil.PostInfo(accout, editText1.getText().toString(), editText2.getText().toString(),
                        editText3.getText().toString(),editText4.getText().toString(), new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("PostInfo","修改失败");
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
                                    Toast.makeText(myInfo.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                SharedPreferences sprfMain= getSharedPreferences("LoginService",MODE_PRIVATE);
                SharedPreferences.Editor editorMain = sprfMain.edit();
                if (!editText4.getText().toString().equals(""))
                    editorMain.putString("password",editText4.getText().toString());
                editorMain.apply();
            }
        });
    }

    //添加动态权限
    private void DongTaiShare() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS, Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            // 调用相机后返回
            case 1:
                if (resultCode == RESULT_OK) {
                    final Bitmap photo = intent.getParcelableExtra("data");
                    //给头像设置你相机拍的照片
                    mIcon.setImageBitmap(photo);
                    // 获取图片 文件 保存
                    File file = getFile(photo);
                    OkHttpUtil.httpsPostImgRequest(accout, file, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String responseData = response.body().string();
                            //在主线程更新ui和响应用户操作
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        Toast.makeText(myInfo.this,"200",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
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
                    ContentResolver cr = getContentResolver();
                    try {
                        //获取图片
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        mIcon.setImageBitmap(bitmap);
                        File file = getFile(bitmap);
                        OkHttpUtil.httpsPostImgRequest(accout, file, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                final String responseData = response.body().string();
                                //在主线程更新ui和响应用户操作
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(myInfo.this,"200",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                } else {
                    //操作错误或没有选择图片
                    Log.i("MainActivtiy", "operation error");
                }
                break;
            default:
                break;
        }
    }

    public void getJsonData(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            editText1.setText(jsonObject.getString("name"));
            editText2.setText(jsonObject.getString("age"));
            editText3.setText(jsonObject.getString("sex"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    //bitmap2File
    public File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //byte数组转图片
    private Bitmap bytesToImageFile(byte[] bytes) {
        Bitmap bitmap = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/facenew1.png");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
            bitmap= BitmapFactory.decodeFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
