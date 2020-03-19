package com.example.yichuguanjia2.ViewPager_inspiration;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.adapter.RecyclerViewAdapter;
import com.example.yichuguanjia2.base.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private String path = "http://api01.6bqb.com/taobao/search?" +
            "apikey=CE2208003CF5AD7252178CD3E291A7F6&keyword=%e6%98%a5%e5%ad%a3%e5%a5%b3%e8%a3%85" +
            "&page=1&order=default";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让推荐中的图片自适应

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_viewpager,container,false);

        recyclerView = view.findViewById(R.id.recycler_view1);
        progressBar = view.findViewById(R.id.fragment_progress);
        messageText = view.findViewById(R.id.fragment_message);
        swipeRefreshLayout = view.findViewById(R.id.mswipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });


        //GridLayoutManager manager = new GridLayoutManager (getContext(),2);
        //recyclerView.setLayoutManager(manager);


        //声明为瀑布流的布局，2列，垂直方向
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
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_i = jsonArray.getJSONObject(i);
                String title = jsonObject_i.get("title").toString();

                JSONArray imageUrlArray = jsonObject_i.getJSONArray("imageUrls");
                String imageUrl = "http:";
                imageUrl = imageUrl + imageUrlArray.get(0).toString();

                Log.e("image",title + imageUrl);
                Image image = new Image(title,imageUrl);
                imageList.add(image);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    messageText.setVisibility(View.GONE);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(),imageList);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}