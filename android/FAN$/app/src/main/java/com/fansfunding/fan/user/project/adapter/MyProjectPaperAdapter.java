package com.fansfunding.fan.user.project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fansfunding.fan.user.project.fragment.*;


/**
 * Created by 13616 on 2016/5/28.
 * 处理我的项目的paperAdapter
 */
public class MyProjectPaperAdapter extends FragmentStatePagerAdapter {

    public MyProjectPaperAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return ProjectPublishFragment.newInstance();
            case 1:
                return ProjectFollowFragment.newInstance();
            case 2:
                return ProjectSupportFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "发起";
            case 1:
                return "关注";
            case 2:
                return "支持";
        }
        return null;
    }
}
