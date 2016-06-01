package com.fansfunding.fan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    public static final int PROJECT=0;
    public static final int PEOPLE=1;


    private int mode;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(int mode) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt("mode",mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mode=getArguments().getInt("mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_search, container, false);

        ListView listView=(ListView)rootView.findViewById(R.id.lv_search);
        SimpleAdapter simpleAdapter=null;
        if(mode==PEOPLE){
            List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
            for(int i=0;i<10;i++){
                Map<String,Object> tempMap=new HashMap<String, Object>();
                listItems.add(tempMap);
            }
            simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_search_people,
                    new String[]{},
                    new int[]{});

        }
        else if(mode==PROJECT){
            //构建simpleadapter
            List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
            for(int i=0;i<10;i++){
                Map<String,Object> tempMap=new HashMap<String, Object>();
                tempMap.put("tv_PJName","项目名称");
                tempMap.put("iv_PJImage",R.drawable.project_image_small_test);
                tempMap.put("tv_PJIntro","这是一个简介");
                tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"10000");
                tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"10000");
                listItems.add(tempMap);
            }
            simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_project,
                    new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                    new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});

        }

        if(simpleAdapter!=null){
            listView.setAdapter(simpleAdapter);
        }
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
