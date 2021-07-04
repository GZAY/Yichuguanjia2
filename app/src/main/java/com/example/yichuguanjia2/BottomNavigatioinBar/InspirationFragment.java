package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.yichuguanjia2.MainActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.Utils.KeyBoardUtils;
import com.example.yichuguanjia2.Utils.ScreenUtils;
import com.example.yichuguanjia2.ViewPager_inspiration.Recommend;
import com.example.yichuguanjia2.ViewPager_inspiration.SearchMore;
import com.example.yichuguanjia2.ViewPager_inspiration.Spring;

import com.example.yichuguanjia2.base.BaseFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class InspirationFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private TabLayout.Tab Recommend;
    private TabLayout.Tab Spring;

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView search;
    PopupWindow popupWindow;
    Context context;
    Toolbar toolbar;
    Drawable drawable;
    List<Fragment> fragments =new ArrayList<Fragment>();
    Fragment fragment;
    String tag;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        drawable = getResources().getDrawable(R.drawable.common_search_ic);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.inspiration, container, false);
        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
        toolbar = view.findViewById(R.id.toolbar);
        search = view.findViewById(R.id.inspiration_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopupWindow();
                // 设置相对View的偏移，1、相对的view，2、相对view的x方向偏移，3、相对view的y方向偏移
                popupWindow.showAsDropDown(new View(getContext()), 0, ScreenUtils.getStatusHeight(getContext()));
                //打开软键盘
                KeyBoardUtils.openKeyboard(new Handler(), 0, context);
            }
        });
        navigationView.setCheckedItem(R.id.nav_mine);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mTablayout = view.findViewById(R.id.inspiration_tab_layout);
        mViewPager = view.findViewById(R.id.inspiration_view_pager);
        if (mViewPager.getAdapter() != null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragments = fm.getFragments();
            if(fragments != null && fragments.size() >0){
                for (int i = 0; i < fragments.size(); i++) {
                    ft.remove(fragments.get(i));
                }
            }
            ft.commit();}
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            private String[] mTitles = new String[]{"今日推荐", "我的收藏"};

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new Recommend();
                    case 1:
                        return new Spring();
                    default:
                        break;
                }
                return new Recommend();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                fragment= (Fragment) super.instantiateItem(container, 1);
                Fragment fragment2= (Fragment) super.instantiateItem(container, 0);
                tag = fragment2.getClass().getName();
                return super.instantiateItem(container,position);
            }
        };
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);
        Recommend = mTablayout.getTabAt(0);
        Spring = mTablayout.getTabAt(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println(position);
                switch (position) {
                    case 0:
                        Recommend.select();
                        break;
                    case 1:
                        adapter.notifyDataSetChanged();
                        Spring.select();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
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

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchContent = searchEt.getText().toString();
                    if (TextUtils.isEmpty(searchContent)) {
                        return false;
                    } else {
                        Intent intent = new Intent(getActivity(), SearchMore.class);
                        intent.putExtra("search",searchContent);
                        startActivity(intent);
                    }
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        selectTypeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo someThing
            }
        });

        view.findViewById(R.id.search_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = searchEt.getText().toString();
                Intent intent = new Intent(getActivity(), SearchMore.class);
                intent.putExtra("search",searchContent);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        // PopupWindow的消失事件监听，消失的时候，关闭软键盘
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                KeyBoardUtils.closeKeybord(context);
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
