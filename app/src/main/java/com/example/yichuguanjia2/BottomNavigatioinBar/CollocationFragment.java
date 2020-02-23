package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        return R.layout.collocation;
    }

    @Override
    protected void initViews() {
    }

    Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//加上这句话，menu才会显示出来
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.toolbar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = findViewById(R.id.toolbar1); //最关键一步在onViewCreated中初始化，而不是在onCreatedView中
    }

    //Toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        toolbar.getMenu().clear();
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.test_menu1:
                Toast.makeText(getActivity(), "backup", Toast.LENGTH_SHORT).show();
            case R.id.test_menu2:
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
            case R.id.test_menu3:
                Toast.makeText(getActivity(), "menu", Toast.LENGTH_SHORT).show();
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
