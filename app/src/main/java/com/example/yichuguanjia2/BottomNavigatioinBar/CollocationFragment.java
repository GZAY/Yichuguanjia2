package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.yichuguanjia2.MainActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.gson.cityId;
import com.example.yichuguanjia2.weather.gson.Weather;
import com.example.yichuguanjia2.weather.service.AutoUpdateService;
import com.example.yichuguanjia2.weather.util.HttpUtil;
import com.example.yichuguanjia2.weather.util.Utility;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static org.litepal.LitePalBase.TAG;


public class CollocationFragment extends Fragment {
    protected View mRootView;
    public DrawerLayout drawerLayout;
    public SwipeRefreshLayout swipeRefresh;
    private LinearLayout weatherLayout;
    private ImageView navButton;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView comfortText;
    private ImageView bingPicImg;
    private String mWeatherId;
    public Activity mActivity;
    private ImageView image;
    private ImageView imageCoat;
    private ImageView imageDown;
    public double LongitudeId = 116.4052887;//经度
    public double LatitudeId = 39.90498734;//纬度

    public LocationClient mLocationClient;
    private MyLocationListener myListener = new MyLocationListener();
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mActivity=(Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collocation,container,false);
        // 初始化各控件
        bingPicImg = mRootView.findViewById(R.id.bing_pic_img);
        weatherLayout = mRootView.findViewById(R.id.weather_layout);
        titleCity = mRootView.findViewById(R.id.title_city);
        titleUpdateTime = mRootView.findViewById(R.id.title_update_time);
        degreeText = mRootView.findViewById(R.id.degree_text);
        weatherInfoText = mRootView.findViewById(R.id.weather_info_text);
        comfortText = mRootView.findViewById(R.id.comfort_text);
        swipeRefresh = mRootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = mRootView.findViewById(R.id.drawer_layout);
        navButton = mRootView.findViewById(R.id.nav_button);
        requestLocation();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
            weatherString = prefs.getString("weather1", null);
            weather = Utility.handleWeatherResponse(weatherString);
            String comfort = "舒适度：" + weather.comfort.get(1).txt;
            comfortText.setText(comfort);
            weatherLayout.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getActivity(), AutoUpdateService.class);
            getActivity().startService(intent);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = getActivity().getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestLocation();
                requestWeather(mWeatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });
        /*String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }*/

        initFloatButton(mRootView);
        imageCoat = mRootView.findViewById(R.id.coat);
        imageCoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = (ImageView) v;
                openAlbum();
            }
        });
        imageDown = mRootView.findViewById(R.id.down);
        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = (ImageView) v;
                openAlbum();
            }
        });

        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定位监听器，一旦调用requestLocation()函数，就会触发MyLocationListener()
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        List<String> permissionList=new ArrayList<>();//用于把3个权限的判断生成list传递出去判断
//        权限判断
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty())
        {
            String [] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }
        else
        {
            requestLocation();
        }
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(String weatherId) {
        //weatherId = CityID;
        String weatherUrl1 = "https://free-api.heweather.net/s6/weather/lifestyle?location="+LongitudeId+","+LatitudeId+"&key=d91646f10b8f44e9a2d10de03fcaa524";
        String weatherUrl = "https://free-api.heweather.net/s6/weather/now?location="+LongitudeId+","+LatitudeId+"&key=d91646f10b8f44e9a2d10de03fcaa524";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d(TAG, "收到信息"+responseText);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        HttpUtil.sendOkHttpRequest(weatherUrl1, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("weather1", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;

                            String comfort = "舒适度：" + weather.comfort.get(1).txt;
                            comfortText.setText(comfort);
                            weatherLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(getActivity(), AutoUpdateService.class);
                            getActivity().startService(intent);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        /*String requestBingPic = "http://guolin.tech/api/bing_pic";
        com.example.yichuguanjia2.HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mActivity).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });*/
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = "00:00";
        updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
    }

    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton mapTypeBtn, locModeBtn, panoramaBtn;
    private void initFloatButton(View view) {
        mFloatingActionsMenu = view.findViewById(R.id.map_actions_menu);
        mapTypeBtn = view.findViewById(R.id.change_map_type);
        mapTypeBtn.setIcon(R.drawable.c_move);
        mapTypeBtn.setTitle("自由移动");
        mapTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionsMenu.toggle();
            }
        });

        locModeBtn = view.findViewById(R.id.change_map_model);
        locModeBtn.setIcon(R.drawable.c_move);
        locModeBtn.setTitle("保存搭配");
        locModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionsMenu.toggle();
            }
        });

        locModeBtn = view.findViewById(R.id.position_panorama);
        locModeBtn.setIcon(R.drawable.c_move);
        locModeBtn.setTitle("清空搭配");
        locModeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                imageCoat.setBackground(null);
                imageDown.setBackground(null);
                imageCoat.setImageDrawable(getResources().getDrawable(R.drawable.c_coat0));
                imageDown.setImageDrawable(getResources().getDrawable(R.drawable.c_down0));
                mFloatingActionsMenu.toggle();
            }
        });
        /*panoramaBtn = view.findViewById(R.id.position_panorama);
        panoramaBtn.setIcon(R.drawable.c_move);
        panoramaBtn.setTitle("其他操作");
        panoramaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    //打开相册
    private void openAlbum() {
        if (Build.VERSION.SDK_INT >= 23){
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 300);
        }else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,300);  //打开相册
        }
    }
    /**
     *把用户选择的图片显示在imageview中
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if(resultCode==RESULT_OK){
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = getContext().getContentResolver();
            try {
                //获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }else{
            //操作错误或没有选择图片
            Log.i("MainActivtiy", "operation error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取定位信息
     */
    //    定位函数
    private void requestLocation()
    {
        initLocation();//定位初始化
        mLocationClient.start();//开始定位
    }
    //    定位初始化
    private void initLocation()
    {
        LocationClientOption option=new LocationClientOption();
        option.setIsNeedAltitude(true);
        option.setOpenGps(true);
        option.setOpenAutoNotifyMode();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//开启GPS定位
        mLocationClient.setLocOption(option);
    }

    //    请求权限小窗口
    @Override
    public void onRequestPermissionsResult(int requestCode,String [] permissions, int [] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    for (int result:grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(getActivity(),"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();//权限同意之后进行定位
                }else
                {
                    Toast.makeText(getActivity(),"发生未知错误",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    //    定位结果信息赋值
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            LongitudeId=location.getLongitude();//经度获取
            LatitudeId=location.getLatitude();//纬度获取

            Log.d(TAG, "经度"+LongitudeId);
            Log.d(TAG, "纬度"+LatitudeId);
            int errorCode = location.getLocType();
            Log.d(TAG, errorCode+"");
            //requestCityInfo(LongitudeId,LatitudeId);//根据经纬度，请求服务器
        }
    }

    //    用经纬度向服务器请求获取城市json
    /*public void requestCityInfo(double longitude,double latitude)
    {
        String cityUrl="https://search.heweather.net/find?location="+longitude+","+latitude+"&key=8e669fb35db1436496ad76e9aec7ba60";
        System.out.println("请求链接："+cityUrl);
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseCityInfoText=response.body().string();
                System.out.println("返回的信息："+responseCityInfoText);
                //把返回的数据交到Utility进行Gson解析
                cityId CITYID = Utility.handleCityIdResponse(responseCityInfoText);
                String N = "{\"HeWeather6\":[{\"status\":\"unknown location\"}]}";
                if(!responseCityInfoText.equals(N)){
                    //根据当前经纬度得出的城市的ID，可利用该ID直接向和风天气API请求该城市的天气信息
                    CityID=CITYID.basicsList.get(0).cityID;
                    System.out.println("最后的一步，ID："+CityID);
                    mLocationClient.stop();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"经纬度请求城市信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }*/
}
