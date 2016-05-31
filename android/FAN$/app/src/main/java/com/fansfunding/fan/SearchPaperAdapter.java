package com.fansfunding.fan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 13616 on 2016/5/31.
 * 搜索界面中viewpaper的paperadapter
 */
public class SearchPaperAdapter extends FragmentPagerAdapter {

    public SearchPaperAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SearchFragment.newInstance(0);
            case 1:
                return SearchFragment.newInstance(1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "项目";
            case 1:
                return "人";
        }
        return super.getPageTitle(position);
    }
}
