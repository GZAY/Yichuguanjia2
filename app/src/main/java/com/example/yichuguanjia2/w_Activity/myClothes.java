package com.example.yichuguanjia2.w_Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class myClothes extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private CircleImageView mIcon;
    private EditText edit_color;
    private TextView spinner_scene;
    private TextView spinner_season;
    private TextView spinner_size;
    private TextView spinner_type;
    private EditText brand;
    private LinearLayout cancel;
    private Button updateInfo;
    final List<String> mOptionsItems1 = new ArrayList<>();
    final List<String> mOptionsItems2 = new ArrayList<>();
    final List<String> mOptionsItems3 = new ArrayList<>();
    final List<String> mOptionsItems4 = new ArrayList<>();
    private File file;
    private OptionsPickerView typeOptions; //定义选择器
    private OptionsPickerView sizeOptions; //定义选择器
    private OptionsPickerView seasonOptions; //定义选择器
    private OptionsPickerView sceneOptions; //定义选择器
    @Override
    protected void initFindViewById() {
        back = findViewById(R.id.about_us_back);
        mIcon = findViewById(R.id.header_image);
        edit_color = findViewById(R.id.edit_color);
        spinner_scene = findViewById(R.id.spinner_scene);
        spinner_season = findViewById(R.id.spinner_season);
        spinner_size = findViewById(R.id.spinner_size);
        spinner_type = findViewById(R.id.spinner_type);
        brand = findViewById(R.id.edit_brand);
        cancel = findViewById(R.id.cancel);
        updateInfo = findViewById(R.id.updateInfo);
        LinearLayout ll_type = findViewById(R.id.ll_type);
        ll_type.setOnClickListener(this);
        LinearLayout ll_size = findViewById(R.id.ll_size);
        ll_size.setOnClickListener(this);
        LinearLayout ll_season = findViewById(R.id.ll_season);
        ll_season.setOnClickListener(this);
        LinearLayout ll_scene = findViewById(R.id.ll_scene);
        ll_scene.setOnClickListener(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.myclothes;
    }

    @Override
    protected void initViews() {
        initTypeOptions();
        initSizeOptions();
        initSeasonOptions();
        initSceneOptions();
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
                final MyDialog mMyDialog = new MyDialog(myClothes.this, 0, 0, view, R.style.DialogTheme);
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
                brand.clearFocus();
            }
        });
        UI_Tools ui_tools = new UI_Tools();
        ui_tools.CancelFocusOne(this, cancel, brand);
        SharedPreferences sprfMain = getSharedPreferences("LoginService", MODE_PRIVATE);
        final String id = sprfMain.getString("id", "1");
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final Date date = new Date(System.currentTimeMillis());
                final String time = simpleDateFormat.format(date);
                OkHttpUtil.addClothes(id, spinner_type.getText().toString(), spinner_scene.getText().toString(),
                        spinner_size.getText().toString(), edit_color.getText().toString(), time,
                        spinner_season.getText().toString(), brand.getText().toString(), file, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("PostInfo", "保存失败");
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
                                        Toast.makeText(myClothes.this, "保存成功", Toast.LENGTH_SHORT).show();
                                        myClothes.this.finish();
                                    }
                                });
                            }
                        });
            }
        });
        if (getIntent().getStringExtra("image") != null) {
            Uri uri = getMediaUriFromPath(this, getIntent().getStringExtra("image"));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                mIcon.setImageBitmap(bitmap);
                file = getFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_type:
                typeOptions.show(); //显示选择器
                break;
            case R.id.ll_size:
                sizeOptions.show();
                break;
            case R.id.ll_season:
                seasonOptions.show();
                break;
            case R.id.ll_scene:
                sceneOptions.show();
                break;
            default:
                break;
        }
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
                    file = getFile(photo);
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
                        file = getFile(bitmap);
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

    public void getJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            /*brand.setText(jsonObject.getString("name"));
            editText2.setText(jsonObject.getString("age"));
            editText3.setText(jsonObject.getString("sex"));*/
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
            bitmap = BitmapFactory.decodeFile(file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /***
     * 将指定路径的图片转uri
     * @param context
     * @param path ，指定图片(或文件)的路径
     * @return
     */
    public static Uri getMediaUriFromPath(Context context, String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[]{path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if (cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;
    }

    //初始化选择器方法具体参数参考github
    private void initTypeOptions() {
        String[] list1 = {"上衣", "衬衫", "外套", "裤子", "裙子", "鞋", "包"};
        mOptionsItems1.addAll(Arrays.asList(list1));
        //条件选择器初始化
        typeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //这边是确认事件
                spinner_type.setText(mOptionsItems1.get(options1));
            }
        }).setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(0xFFFFA500)//确定按钮文字颜色
                .setCancelColor(0xFFFFA500)//取消按钮文字颜色
                .build();
        typeOptions.setPicker(mOptionsItems1);
    }
    private void initSizeOptions() {
        String[] list = {"S", "M", "L", "XL", "XXL", "XXXL"};
        mOptionsItems2.addAll(Arrays.asList(list));
        //条件选择器初始化
        sizeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //这边是确认事件
                spinner_size.setText(mOptionsItems2.get(options1));
            }
        }).setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(0xFFFFA500)//确定按钮文字颜色
                .setCancelColor(0xFFFFA500)//取消按钮文字颜色
                .build();
        sizeOptions.setPicker(mOptionsItems2);
    }
    private void initSeasonOptions() {
        String[] list = {"春", "夏", "秋", "冬"};
        mOptionsItems3.addAll(Arrays.asList(list));
        //条件选择器初始化
        seasonOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //这边是确认事件
                spinner_season.setText(mOptionsItems3.get(options1));
            }
        }).setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(0xFFFFA500)//确定按钮文字颜色
                .setCancelColor(0xFFFFA500)//取消按钮文字颜色
                .build();
        seasonOptions.setPicker(mOptionsItems3);
    }
    private void initSceneOptions() {
        String[] list = {"工作", "休闲", "派对", "正式", "度假", "运动", "居家"};
        mOptionsItems4.addAll(Arrays.asList(list));
        //条件选择器初始化
        sceneOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //这边是确认事件
                spinner_scene.setText(mOptionsItems4.get(options1));
            }
        }).setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(0xFFFFA500)//确定按钮文字颜色
                .setCancelColor(0xFFFFA500)//取消按钮文字颜色
                .build();
        sceneOptions.setPicker(mOptionsItems4);
    }
}
