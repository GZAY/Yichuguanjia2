package com.example.yichuguanjia2.w_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yichuguanjia2.Login.OkHttpUtil;
import com.example.yichuguanjia2.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class My_case extends AppCompatActivity implements View.OnClickListener, MineRadioAdapter.OnItemClickListener {
    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    private ImageView back;
    private RecyclerView mRecyclerview;
    private TextView mTvSelectNum;
    private Button mBtnDelete;
    private TextView mSelectAll;
    private LinearLayout mLlMycollectionBottomDialog;
    private TextView mBtnEditor;
    private MineRadioAdapter mRadioAdapter = null;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<DataModel> mList = new ArrayList<>();
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
    private List<Clothes> myClothes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_case);
        initView();
        initData();
        initListener();
        getClothes(getIntent().getStringExtra("content"), getIntent().getIntExtra("pos", 1));
    }

    protected void initView() {
        mRecyclerview = findViewById(R.id.m_case_recyclerView);
        mTvSelectNum = findViewById(R.id.tv_select_num);
        mBtnDelete = findViewById(R.id.btn_delete);
        mSelectAll = findViewById(R.id.select_all);
        mLlMycollectionBottomDialog = findViewById(R.id.ll_mycollection_bottom_dialog);
        mBtnEditor = findViewById(R.id.btn_editor);
        back = findViewById(R.id.m_case_back);
    }

    private void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                My_case.this.finish();
            }
        });
        mRadioAdapter = new MineRadioAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_main_bg_height_1));
        mRecyclerview.addItemDecoration(itemDecorationHeader);
        mRecyclerview.setAdapter(mRadioAdapter);
    }


    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
            mBtnDelete.setTextColor(Color.WHITE);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnDelete.setEnabled(false);
            mBtnDelete.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
        }
    }

    protected void initListener() {
        mRadioAdapter.setOnItemClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mBtnEditor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                deleteVideo();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.btn_editor:
                updataEditMode();
                break;
            default:
                break;
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mRadioAdapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(true);
            }
            index = mRadioAdapter.getMyLiveList().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                mRadioAdapter.getMyLiveList().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        mRadioAdapter.notifyDataSetChanged();
        setBtnBackground(index);
        mTvSelectNum.setText(String.valueOf(index));
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0) {
            mBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg = builder.findViewById(R.id.tv_msg);
        Button cancle = builder.findViewById(R.id.btn_cancle);
        Button sure = (Button) builder.findViewById(R.id.btn_sure);
        if (msg == null || cancle == null || sure == null) return;

        if (index == 1) {
            msg.setText("删除后不可恢复，是否删除该条目？");
        } else {
            msg.setText("删除后不可恢复，是否删除这" + index + "个病例？");
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = mRadioAdapter.getMyLiveList().size(), j = 0; i > j; i--) {
                    DataModel myLive = mRadioAdapter.getMyLiveList().get(i - 1);
                    if (myLive.isSelect()) {
                        mRadioAdapter.getMyLiveList().remove(myLive);
                        index--;
                        OkHttpUtil.deleteClothes(myLive.getId().toString(), new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            }
                        });
                    }
                }
                index = 0;
                mTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (mRadioAdapter.getMyLiveList().size() == 0) {
                    mLlMycollectionBottomDialog.setVisibility(View.GONE);
                }
                mRadioAdapter.notifyDataSetChanged();
                builder.dismiss();
                updataEditMode();
            }
        });
    }

    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {
            mBtnEditor.setText("取消");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mBtnEditor.setText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
            clearAll();
        }
        mRadioAdapter.setEditMode(mEditMode);
    }


    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        setBtnBackground(0);
    }

    @Override
    public void onItemClickListener(int pos, ArrayList<DataModel> myLiveList) {
        if (editorStatus) {
            DataModel myLive = myLiveList.get(pos);
            boolean isSelect = myLive.isSelect();
            if (!isSelect) {
                index++;
                myLive.setSelect(true);
                if (index == myLiveList.size()) {
                    isSelectAll = true;
                    mSelectAll.setText("取消全选");
                }

            } else {
                myLive.setSelect(false);
                index--;
                isSelectAll = false;
                mSelectAll.setText("全选");
            }
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
            mRadioAdapter.notifyDataSetChanged();
        }
    }

    private void getClothes(final String key, final int postion) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SharedPreferences sprfMain= getSharedPreferences("LoginService",MODE_PRIVATE);
                final String id = sprfMain.getString("id","1");
                if (postion == 0)
                    OkHttpUtil.allClothes(id, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("PostInfo", "获取失败");
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
                                    message = new Message();
                                    message.what = 0x11;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    });
                else if (postion == 1)
                    OkHttpUtil.findByType(id, key, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("PostInfo", "获取失败");
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
                                message = new Message();
                                message.what = 0x11;
                                handler.sendMessage(message);
                            }
                        });
                    }
                });
                else if (postion == 2)
                    OkHttpUtil.findByScene(id, key, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("PostInfo", "获取失败");
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
                                    message = new Message();
                                    message.what = 0x11;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    });
                else if (postion == 3)
                    OkHttpUtil.findBySeason(id, key, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("PostInfo", "获取失败");
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
                                    message = new Message();
                                    message.what = 0x11;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    });
                else if (postion == 4)
                    OkHttpUtil.findByBrand(id, getIntent().getStringExtra("content"), new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("PostInfo", "获取失败");
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
                                    message = new Message();
                                    message.what = 0x11;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    });
                else if (postion == 5)
                    OkHttpUtil.findByColor(id, getIntent().getStringExtra("content"), new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("PostInfo", "获取失败");
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
                                    message = new Message();
                                    message.what = 0x11;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    });
            }
        }.start();
    }
    public void getJsonData(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Clothes clothes = new Clothes();
                clothes.setId(Integer.parseInt(jsonObject.getString("id")));
                clothes.setType(jsonObject.getString("type"));
                clothes.setScene(jsonObject.getString("scene"));
                clothes.setSeason(jsonObject.getString("season"));
                clothes.setSize(jsonObject.getString("size"));
                clothes.setColor(jsonObject.getString("color"));
                clothes.setTime(jsonObject.getString("time"));
                clothes.setBrand(jsonObject.getString("brand"));
                clothes.setImage(jsonObject.getString("image"));
                myClothes.add(clothes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static Message message;
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                setAdapter();
            }
        }
    };
    void setAdapter(){
        for (Clothes clothes : myClothes) {
            DataModel dataModel = new DataModel();
            dataModel.setId(clothes.getId());
            dataModel.setBrand(clothes.getBrand());
            dataModel.setColor(clothes.getColor());
            dataModel.setImage(clothes.getImage());
            dataModel.setScene(clothes.getScene());
            dataModel.setSize(clothes.getSize());
            dataModel.setTime(clothes.getTime());
            dataModel.setType(clothes.getType());
            dataModel.setSeason(clothes.getSeason());
            mList.add(dataModel);
        }
        mRadioAdapter.notifyAdapter(mList, false);
    }
}
