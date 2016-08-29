package com.fansfunding.fan;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fansfunding.fan.social.fragment.SocialFragment;

/**
 * Created by 13616 on 2016/5/21.
 * 用来处理主界面viewpaper的适配器
 */
public class MainPaperAdapter extends FragmentStatePagerAdapter {

    //界面的数量
    private static final int count=4;

    private boolean isLogin=false;
    Context context;
    public MainPaperAdapter(FragmentManager fm,Context context){
        super(fm);
        this.context=context;
        isLogin=isLogin();
    }

    public boolean isLogin(){
        if(context==null)
            return false;
        SharedPreferences share=context.getSharedPreferences(context.getString(R.string.sharepreference_login_by_phone),Context.MODE_PRIVATE);
        boolean isLogin=share.getBoolean("isLogin",false);

        return isLogin;
    }
    public boolean isNeedChange(){
        if(isLogin!=isLogin()){
            isLogin=!isLogin;
            return true;
        }else {
            return false;
        }
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return CrowdFundingFragment.newInstance();
            case 1:
                return SocialFragment.newInstance();
            case 2:
                String messagePage = "";
                switch (MainActivity.pagePosition) {
                    case 0:
                        messagePage = "privateletter";
                        break;
                    case 1:
                        messagePage = "comment";
                        break;
                    case 2:
                        messagePage = "notification";
                        break;
                    default:
                        break;
                }
                return com.fansfunding.fan.message.fragment.MessageFragment.newInstance(messagePage);

            case 3:
                if(isLogin==true)
                    return UserFragment.newInstance();
                else
                    return UnlogFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }

   /* @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "众筹";
            case 1:
                return "圈子";
            case 2:
                return "我";

        }
        return null;
    }*/

    //重写这个方法
    @Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }
}
