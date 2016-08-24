package com.fansfunding.fan.social.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fansfunding.fan.R;

/**
 * Created by 13616 on 2016/8/19.
 */
public class SocialListAdapter extends BaseAdapter {

    private Activity context;

    public SocialListAdapter(Activity context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView= LayoutInflater.from(context).inflate(R.layout.item_social_gridview,null);
        return rootView;
    }
}
