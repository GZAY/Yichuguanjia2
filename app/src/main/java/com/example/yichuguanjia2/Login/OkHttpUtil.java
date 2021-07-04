package com.example.yichuguanjia2.Login;

import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.util.Log;

import java.io.File;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
    //private static String url = "http://10.0.2.2:8080/";
    private static String url = "http://192.168.137.1:8080/";

    //登陆请求
    public static void loginWithOkHttp(String account,String password,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("account",account)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/signin")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //注册请求
    public static void registerWithOkHttp(String phone, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("phone",phone)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(url + "myCouse/signup")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //上传个人信息
    public static void PostInfo(String phone, String name, String age, String sex, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("phone",phone)
                .add("name",name)
                .add("age",age)
                .add("sex",sex)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(url + "myCouse/updateinfo")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //获取个人信息
    public static void getInfo(String account,String password,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("account",account)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/signin")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //上传图片
    // 我的返回数据组成为　data，return_code mesg
    public static void httpsPostImgRequest(String account, File file, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                // 此处可添加上传 参数
                // photoFile 表示上传参数名,logo.png 表示图片名字
                .addFormDataPart("phone",account)
                .addFormDataPart("photoFile", "logo.png",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))//文件
                .build();
        Request request = new Request.Builder()
                .url(url + "myCouse/picture")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //获取个人信息
    public static void getpic(String account,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("account",account)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/getpic")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addClothes(String userID, String type, String scene, String size, String color, String time, String season, String brand, File file, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                // 此处可添加上传 参数
                // photoFile 表示上传参数名,logo.png 表示图片名字
                .addFormDataPart("type",type)
                .addFormDataPart("scene",scene)
                .addFormDataPart("size",size)
                .addFormDataPart("color",color)
                .addFormDataPart("time",time)
                .addFormDataPart("season",season)
                .addFormDataPart("brand",brand)
                .addFormDataPart("userID",userID)
                .addFormDataPart("image", "image.png",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))//文件
                .build();
        Request request = new Request.Builder()
                .url(url + "myCouse/addClothes")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void allClothes(String userID, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/allClothes")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //获取个人信息
    public static void findBySeason(String userID, String season, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .add("season",season)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/findBySeason")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void findByScene(String userID, String scene, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .add("scene",scene)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/findByScene")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findByColor(String userID, String Color, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .add("Color",Color)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/findByColor")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findByBrand(String userID, String brand, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .add("brand",brand)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/findByBrand")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void findByType(String userID, String type, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userID",userID)
                .add("type",type)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/findByType")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteClothes(String id, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url( url + "myCouse/deleteClothes")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}


