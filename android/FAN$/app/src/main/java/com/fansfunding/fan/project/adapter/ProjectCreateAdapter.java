package com.fansfunding.fan.project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fansfunding.fan.project.fragment.CreateProjectAddRewordFragment;
import com.fansfunding.fan.project.fragment.CreateProjectFragment;
import com.fansfunding.fan.project.fragment.CreateProjectRewordFragment;

/**
 * Created by 13616 on 2016/5/28.
 * 处理我的项目的paperAdapter
 */
public class ProjectCreateAdapter extends FragmentPagerAdapter {

    public ProjectCreateAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return CreateProjectFragment.newInstance();
            case 1:
                return  CreateProjectRewordFragment.newInstance();
            case 2:
                return  CreateProjectAddRewordFragment.newInstance();
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
                return "发起项目";
            case 1:
                return "设置回报";
            case 2:
                return "编辑回报";
        }
        return null;
    }
}
