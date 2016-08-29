package com.fansfunding.fan.message.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.fansfunding.fan.message.fragment.CommentFragment;
import com.fansfunding.fan.message.fragment.NotifacationFragment;
import com.fansfunding.fan.message.fragment.PrivateLetterFragment;

/**
 * Created by RJzz on 2016/8/26.
 */

public class MessageFragmentAdapter extends FragmentStatePagerAdapter {

    private final int COUNT = 3;

    private String[] titles = new String[]{"私信", "评论", "通知"};

    private Context context;

    public MessageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrivateLetterFragment();
            case 1:
                return new CommentFragment();
            case 2:
                return new NotifacationFragment();
            default:
                Log.e("MessageFragment", "在创建三个fragment的时候出现了问题，返回了一个空的东西");
                System.out.println("在创建三个fragment的时候出现了问题，返回了一个空的东西");
                return null;
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
