package com.fansfunding.fan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 13616 on 2016/5/21.
 * 用来处理主界面viewpaper的适配器
 */
public class MainPaperAdapter extends FragmentPagerAdapter {

    public MainPaperAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return CrowdFundingFragment.newInstance();
            case 1:
                return SocialFragment.newInstance();
            case 2:
                return MessageFragment.newInstance();
            case 3:
                return UserFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "众筹";
            case 1:
                return "圈子";
            case 2:
                return "消息";
            case 3:
                return "我";
        }
        return null;
    }

}
