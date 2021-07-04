package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.yichuguanjia2.Utils.KeyBoardUtils;
import com.example.yichuguanjia2.Utils.ScreenUtils;
import com.example.yichuguanjia2.ViewPager_inspiration.SearchMore;
import com.example.yichuguanjia2.spl_clothes.w_clothes1;
import com.example.yichuguanjia2.spl_clothes.w_clothes2;
import com.example.yichuguanjia2.spl_clothes.w_clothes3;
import com.example.yichuguanjia2.w_Activity.My_case;
import com.example.yichuguanjia2.w_Activity.myClothes;
import com.example.yichuguanjia2.w_Activity.w_manage;
import com.example.yichuguanjia2.w_Activity.w_season;
import com.example.yichuguanjia2.w_Activity.w_type;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.spl_clothes.w_clothes4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WardrobeFragment extends Fragment {
    private Toolbar toolbar;
    private View myView;
    private String TAG = "Toolbar";
    private ImageView addClothes;
    private PopupWindow popupWindow;
    private Drawable drawable;
    private ArrayAdapter<String> SinnerAdapter;
    /**
     * Toolbar menu键显示
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setHasOptionsMenu(true);//加上这句话，menu才会显示出来
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        myView = inflater.inflate(R.layout.wardrobe, container, false);
        toolbar = myView.findViewById(R.id.toolbar1);
        addClothes = myView.findViewById(R.id.addClothes);
        LinearLayout w_common_button = myView.findViewById(R.id.w_common_button);
        w_common_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), w_clothes1.class);
                startActivity(intent);
            }
        });
        LinearLayout w_all = myView.findViewById(R.id.w_all);
        w_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), My_case.class);
                intent.putExtra("pos",0);
                startActivity(intent);
            }
        });
        LinearLayout w_type_button = myView.findViewById(R.id.w_type_button);
        w_type_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), w_type.class);
                intent.putExtra("pos",1);
                startActivity(intent);
            }
        });
        LinearLayout w_management_button = myView.findViewById(R.id.w_management_button);
        w_management_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), w_manage.class);
                intent.putExtra("pos",2);
                startActivity(intent);
            }
        });
        LinearLayout w_season_button = myView.findViewById(R.id.w_season_button);
        w_season_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), w_season.class);
                intent.putExtra("pos",3);
                startActivity(intent);
            }
        });
        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), myClothes.class);
                startActivity(intent);
            }
        });
        drawable = getResources().getDrawable(R.drawable.common_search_ic);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ImageView search;
        search = myView.findViewById(R.id.search_main);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopupWindow();
                // 设置相对View的偏移，1、相对的view，2、相对view的x方向偏移，3、相对view的y方向偏移
                popupWindow.showAsDropDown(new View(getContext()), 0, ScreenUtils.getStatusHeight(getContext()));
                //打开软键盘
                KeyBoardUtils.openKeyboard(new Handler(), 0, getContext());
            }
        });
        setView();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉标题
        //toolbar.inflateMenu(R.menu.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        //inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
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



    /**
     * 轮播图
     */
    private View mView;
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.pic_cloth5,
            R.drawable.pic_cloth1,
            R.drawable.pic_cloth2,
            R.drawable.pic_cloth3,
            R.drawable.pic_cloth4
    };
    //存放图片的标题
    private String[] titles = new String[]{"", "", "", "", ""};
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;
    private void setView(){
        mViewPaper = myView.findViewById(R.id.vp);

        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(myView.findViewById(R.id.dot_0));
        dots.add(myView.findViewById(R.id.dot_1));
        dots.add(myView.findViewById(R.id.dot_2));
        dots.add(myView.findViewById(R.id.dot_3));
        dots.add(myView.findViewById(R.id.dot_4));

        title = myView.findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_yes);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_no);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /*定义的适配器*/
    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }

    /**
     * 图片轮播任务
     * @author liuyazhuang
     *
     */
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    /**
     * 初始化PopupWindow
     */
    protected void initPopupWindow() {
        final View view = getLayoutInflater().inflate(R.layout.search_popup, null, false);
        int height = toolbar.getHeight(); //  获取当前页面ToolBar的高度
        final EditText searchEt = (EditText) view.findViewById(R.id.search_et);
        searchEt.setCompoundDrawables(drawable,null,null,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        final RelativeLayout selectTypeRl = (RelativeLayout) view.findViewById(R.id.select_type_rl);
        popupWindow.setFocusable(true);//设置外部点击取消
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 不设置的话不能关闭此 PopupWindow
        popupWindow.setAnimationStyle(R.style.AnimBottom_search);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
        final Spinner spinner_main = view.findViewById(R.id.spinner_main);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchContent = searchEt.getText().toString();
                    if (TextUtils.isEmpty(searchContent)) {
                        return false;
                    }
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        String[] list = {"品牌","颜色"};
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        SinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        SinnerAdapter.setDropDownViewResource(R.layout.item_spinselect);
        //第四步：将适配器添加到下拉列表上
        spinner_main.setAdapter(SinnerAdapter);

        view.findViewById(R.id.search_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String searchContent = searchEt.getText().toString();
            if (spinner_main.getSelectedItem().toString().equals("品牌")){
                Intent intent = new Intent(getActivity(), My_case.class);
                intent.putExtra("pos",4);
                intent.putExtra("content",searchContent);
                startActivity(intent);
            }else if (spinner_main.getSelectedItem().toString().equals("颜色")){
                Intent intent = new Intent(getActivity(), My_case.class);
                intent.putExtra("content",searchContent);
                intent.putExtra("pos",5);
                startActivity(intent);
            }
            popupWindow.dismiss();
            }
        });
        // PopupWindow的消失事件监听，消失的时候，关闭软键盘
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                KeyBoardUtils.closeKeybord(getContext());
            }
        });
    }

    /**
     * 获取PopipWinsow实例
     */
    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow();
        }
    }
}
