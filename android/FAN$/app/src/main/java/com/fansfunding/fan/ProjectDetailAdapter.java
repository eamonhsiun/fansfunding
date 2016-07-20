package com.fansfunding.fan;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fansfunding.internal.AllProjectInCategory;

/**
 * Created by 13616 on 2016/7/17.
 */
public class ProjectDetailAdapter extends FragmentStatePagerAdapter {

    //项目信息
    private AllProjectInCategory.ProjectDetail detail;

    public ProjectDetailAdapter(FragmentManager fm, AllProjectInCategory.ProjectDetail detail){
        super(fm);
        this.detail=detail;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ProjectDetailMainFragment.newInstance(detail);
            case 1:
                return ProjectDetailRewardFragment.newInstance(detail);
            case 2:
                return ProjectDetailDynamicFragment.newInstance(detail);
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
