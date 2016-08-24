package com.fansfunding.fan.social.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.fansfunding.fan.social.fragment.MomentCommentFragment;

import java.util.List;

/**
 * Created by 13616 on 2016/8/20.
 */
public class MomentPaperAdapter extends FragmentStatePagerAdapter {

    private final int count=2;

    private List<ScrollableHelper.ScrollableContainer> fragmentList;

    public MomentPaperAdapter(FragmentManager fm,List<ScrollableHelper.ScrollableContainer> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
    }

    public void setFragmentList(List<ScrollableHelper.ScrollableContainer> fragmentList){
        this.fragmentList=fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return (Fragment)fragmentList.get(0);
            case 1:
                return (Fragment)fragmentList.get(1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "评论";
            case 1:
                return "赞";
        }
        return null;
    }

    //重写这个方法
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
