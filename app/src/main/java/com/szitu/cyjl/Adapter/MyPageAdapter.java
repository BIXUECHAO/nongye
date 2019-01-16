package com.szitu.cyjl.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

public class MyPageAdapter extends FragmentPagerAdapter {
    List<Fragment> list;
    public MyPageAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        list=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
