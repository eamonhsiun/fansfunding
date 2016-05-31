package com.fansfunding.fan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 13616 on 2016/5/26.
 */
public class MessagePaperAdapter extends FragmentPagerAdapter {

    public MessagePaperAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MessageListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "小组";
            case 1:
                return "系统";
        }
        return super.getPageTitle(position);
    }
}
