package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.yichuguanjia2.MainActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseFragment;
import com.example.yichuguanjia2.weather.HttpUtil;
import com.example.yichuguanjia2.weather.Province;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CollocationFragment extends BaseFragment {

    @Override
    protected int attachLayoutRes() {
        Log.d("cha", "attachLayoutRes: 646");
        return R.layout.collocation;
    }

    @Override
    protected void initViews() {
    }
}
