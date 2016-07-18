package com.fansfunding.fan;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 13616 on 2016/7/17.
 */
public class ProjectDetailAdapter extends FragmentStatePagerAdapter {

    public ProjectDetailAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ProjectDetailMainFragment.newInstance();
            case 1:
                return ProjectDetailRewardFragment.newInstance();
            case 2:
                return ProjectDetailDynamicFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "详情";
            case 1:
                return "回报";
            case 2:
                return "动态";

        }
        return null;
    }
}
