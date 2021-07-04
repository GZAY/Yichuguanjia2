package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.yichuguanjia2.Login.LoginService;
import com.example.yichuguanjia2.Login.OkHttpUtil;
import com.example.yichuguanjia2.M_Activity.myInfo;
import com.example.yichuguanjia2.M_Activity.DataBuckup;
import com.example.yichuguanjia2.M_Activity.M_canlender;
import com.example.yichuguanjia2.M_Activity.M_set;
import com.example.yichuguanjia2.M_Activity.QuestionFeedBack;
import com.example.yichuguanjia2.M_Activity.child_management;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MineFragment extends Fragment {
    private CircleImageView mIcon;
    private View view;
    private TextView user_name;
    private String username;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.CAMERA"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine, container, false);
        mIcon = view.findViewById(R.id.header_image);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        LinearLayout clothes_plan = view.findViewById(R.id.clothes_plan);
        clothes_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), M_canlender.class);
                startActivity(intent);
            }
        });
        LinearLayout child_manage = view.findViewById(R.id.child_management);
        child_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), child_management.class);
                intent.putExtra("version", 15);
                startActivity(intent);
            }
        });
        LinearLayout select_set = view.findViewById(R.id.select_set);
        select_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), M_set.class);
                startActivity(intent);
            }
        });
        LinearLayout data_backup = view.findViewById(R.id.data_backup);
        data_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataBuckup.class);
                startActivity(intent);
            }
        });
        LinearLayout feedback = view.findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuestionFeedBack.class);
                startActivity(intent);
            }
        });
        LinearLayout about_us = view.findViewById(R.id.myInfo);
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), myInfo.class);
                startActivity(intent);
            }
        });
        LinearLayout logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sprfMain= getActivity().getSharedPreferences("LoginService",MODE_PRIVATE);
                SharedPreferences.Editor editorMain = sprfMain.edit();
                editorMain.putBoolean("Login",false);
                editorMain.apply();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginService.class);
                startActivity(intent);
            }
        });
        user_name = view.findViewById(R.id.user_name);
        SharedPreferences sprfMain= getActivity().getSharedPreferences("LoginService",MODE_PRIVATE);
        String accout = sprfMain.getString("account", null);
        String password = sprfMain.getString("password", null);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = bytesToImageFile(responseData);
                        mIcon.setImageBitmap(bitmap);
                    }
                });
            }
        });
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getJsonData(responseData);
                    }
                });
            }
        });
        return view;
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

    public void getJsonData(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            user_name.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

