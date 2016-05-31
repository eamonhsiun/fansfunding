package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来显示我的圈子的界面
 */
public class SocialFragment extends Fragment {

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance() {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_social, container, false);
        Toolbar toolbar=(Toolbar)rootView.findViewById(R.id.toolbar_social);
        toolbar.setTitle("圈子");


        ListView listView=(ListView)rootView.findViewById(R.id.lv_social);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<2;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("social_head",R.drawable.pjimagetest);
            tempMap.put("social_name","昵称");
            tempMap.put("social_time","刚刚");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_social,
                new String[]{"social_head","social_name","social_time"},
                new int[]{R.id.iv_social_head,R.id.tv_social_name,R.id.tv_social_time});


        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_news_detail));
                startActivity(intent);
            }
        });

        FloatingActionButton fab=(FloatingActionButton)rootView.findViewById(R.id.fab_social_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
