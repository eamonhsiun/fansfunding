package com.fansfunding.fan;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
  * Use the {@link ProjectPublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来显示我发起的项目的的界面
 */
public class ProjectPublishFragment extends Fragment {

    public ProjectPublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectPublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectPublishFragment newInstance() {
        ProjectPublishFragment fragment = new ProjectPublishFragment();
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
        for(int i=0;i<2;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("iv_PJImage",R.drawable.project_image_small_test);
            tempMap.put("tv_PJIntro","这是一个简介");
            tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"1000");
            tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"1000");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_project,
                new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});



        View listfoot=inflater.inflate(R.layout.fragment_project_publish, null, false);

        TextView tv_PJ_publish=(TextView)listfoot.findViewById(R.id.tv_PJ_publish);
        tv_PJ_publish.setPaintFlags(Paint. UNDERLINE_TEXT_FLAG);

        listView.addFooterView(listfoot);
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
