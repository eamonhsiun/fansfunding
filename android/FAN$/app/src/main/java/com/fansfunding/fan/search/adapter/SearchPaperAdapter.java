package com.fansfunding.fan.search.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fansfunding.fan.search.fragment.SearchProjectFragment;
import com.fansfunding.fan.search.fragment.SearchUserFragment;


/**
 * Created by 13616 on 2016/5/31.
 * 搜索界面中viewpaper的paperadapter
 */
public class SearchPaperAdapter extends FragmentStatePagerAdapter {

    private String keyword="";

    public void setKeyword(String keyword){
        this.keyword=keyword;
    }

    public SearchPaperAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SearchProjectFragment.newInstance(keyword);
            case 1:
                return SearchUserFragment.newInstance(keyword);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "项目";
            case 1:
                return "用户";
        }
        return super.getPageTitle(position);
    }

    //重写这个方法
    @Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }
}
