package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.ViewPager_inspiration.Autumn;
import com.example.yichuguanjia2.ViewPager_inspiration.Collect;
import com.example.yichuguanjia2.ViewPager_inspiration.Recommend;
import com.example.yichuguanjia2.ViewPager_inspiration.Spring;
import com.example.yichuguanjia2.ViewPager_inspiration.Summer;
import com.example.yichuguanjia2.ViewPager_inspiration.Winter;
import com.example.yichuguanjia2.base.BaseFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class InspirationFragment extends Fragment {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private TabLayout.Tab Recommend;
    private TabLayout.Tab Spring;
    private TabLayout.Tab Summer;
    private TabLayout.Tab Autumn;
    private TabLayout.Tab Winter;
    private TabLayout.Tab Collect;

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.inspiration, container, false);
        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
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
            List<Fragment> fragments = fm.getFragments();
            if(fragments != null && fragments.size() >0){
                for (int i = 0; i < fragments.size(); i++) {
                    ft.remove(fragments.get(i));
                }
            }
            ft.commit();}
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            private String[] mTitles = new String[]{"今日推荐", "春季", "夏季", "秋季", "冬季", "我的收藏"};

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new Recommend();
                    case 1:
                        return new Spring();
                    case 2:
                        return new Summer();
                    case 3:
                        return new Autumn();
                    case 4:
                        return new Winter();
                    case 5:
                        return new Collect();
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

        });
        mTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTablayout.setupWithViewPager(mViewPager);
        Recommend = mTablayout.getTabAt(0);
        Spring = mTablayout.getTabAt(1);
        Summer = mTablayout.getTabAt(2);
        Autumn = mTablayout.getTabAt(3);
        Winter = mTablayout.getTabAt(4);
        Collect = mTablayout.getTabAt(5);
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
                        Spring.select();
                        break;
                    case 2:
                        Summer.select();
                        break;
                    case 3:
                        Autumn.select();
                        break;
                    case 4:
                        Winter.select();
                        break;
                    case 5:
                        Collect.select();
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
}
