package com.example.yichuguanjia2.ViewPager_inspiration;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.adapter.RecyclerViewAdapter;
import com.example.yichuguanjia2.base.Image;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Recommend extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView messageText;
    private List<Image> imageList = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerViewAdapter adapter;
    private int page = 1;
    private ClassicsFooter footer;
    private String path = "https://api03.6bqb.com/taobao/search?" +
            "apikey=CE2208003CF5AD7252178CD3E291A7F6&keyword=%E9%9D%92%E5%B0%91%E5%B9%B4%E6%9C%8D%E8%A3%85" +
            "&page="+page+"&order=default&tab=all";
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recommend_viewpager,container,false);
        recyclerView = view.findViewById(R.id.recycler_view1);
        progressBar = view.findViewById(R.id.fragment_progress);
        messageText = view.findViewById(R.id.fragment_message);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {//下拉刷新
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                imageList.clear();
                page = 1;
                requestData();
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {//上拉加载
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.d("test", "onLoadMore: ");
                page ++ ;
                path = "https://api03.6bqb.com/taobao/search?" +
                        "apikey=CE2208003CF5AD7252178CD3E291A7F6&keyword=%E9%9D%92%E5%B0%91%E5%B9%B4%E6%9C%8D%E8%A3%85" +
                        "&page="+page+"&order=default&tab=all";
                requestData();
                adapter.notifyDataSetChanged();//添加或者修改数据，更新ListView
                refreshLayout.finishLoadMore(true);//加载完成
                footer = view.findViewById(R.id.footer);
                footer.setFinishDuration(500);//时间参数，将参数设为0，就没用白框了
            }
        });


        //瀑布流布局，2列，垂直方向
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager); //recyclerView设置布局管理器
        progressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.VISIBLE);
        requestData();
        return view;
    }


    //数据请求
    private void requestData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //请求链接
                    Request request = new Request.Builder().url(path).build();
                    //获取相应
                    Response response = client.newCall(request).execute();
                    //通过响应获取json数据
                    String responseData = response.body().string();
                    //解析数据
                    parseJson(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //解析Json数据
    private void parseJson(final String responseData){
        try {
            JSONObject jsonObject = new JSONObject(responseData);//将JSON数据转化成JSON对象
            JSONArray jsonArray = jsonObject.getJSONArray("data");//对象数组
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject_i = jsonArray.getJSONObject(i);
                //普通的键值对
                String title = jsonObject_i.get("title").toString();
                //对象数组
                JSONArray imageUrlArray = jsonObject_i.getJSONArray("imageUrls");
                String imageUrl = "";
                imageUrl = imageUrl + imageUrlArray.get(0).toString();

                //Log.e("image",title + imageUrl);
                Image image = new Image(title,"https://" + imageUrl);
                imageList.add(image);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.finishRefresh();
                    progressBar.setVisibility(View.GONE);
                    messageText.setVisibility(View.GONE);

                    adapter = new RecyclerViewAdapter(getContext(),imageList);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    // 设置数据后就要给RecyclerView设置点击事件
                    adapter.setOnItemClickListener(new RecyclerViewAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            // 这里本来是跳转页面 ，我们就在这里直接让其弹toast来演示
                            Toast.makeText(getContext() , imageList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}