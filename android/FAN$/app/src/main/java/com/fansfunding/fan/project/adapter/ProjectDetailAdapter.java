package com.fansfunding.fan.project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fansfunding.fan.project.fragment.ProjectDetailDynamicFragment;
import com.fansfunding.fan.project.fragment.ProjectDetailRewardFragment;
import com.fansfunding.fan.project.fragment.ProjectDetailWebFragment;
import com.fansfunding.internal.ProjectInfo;

/**
 * Created by 13616 on 2016/7/17.
 */
public class ProjectDetailAdapter extends FragmentStatePagerAdapter {

    //项目信息
    private ProjectInfo detail;

    public ProjectDetailAdapter(FragmentManager fm, ProjectInfo detail){
        super(fm);
        this.detail=detail;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ProjectDetailWebFragment.newInstance(detail);
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
