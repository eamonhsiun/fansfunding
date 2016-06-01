package com.fansfunding.fan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来显示我的项目的界面
 */
public class ProjectFragment extends Fragment {

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
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

        View rootView=inflater.inflate(R.layout.fragment_project, container, false);

        ListView listView=(ListView)rootView.findViewById(R.id.lv_PJ_list);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("iv_PJImage",R.drawable.project_image_small_test);
            tempMap.put("tv_PJIntro","这是一个简介");
            tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"10000");
            tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"10000");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_project,
                new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});


        listView.setAdapter(simpleAdapter);


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
