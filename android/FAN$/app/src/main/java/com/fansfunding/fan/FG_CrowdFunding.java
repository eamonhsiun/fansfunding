package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FG_CrowdFunding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FG_CrowdFunding extends Fragment {
    //private OnFragmentInteractionListener mListener;

    public FG_CrowdFunding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FG_CrowdFunding.
     */
    // TODO: Rename and change types and number of parameters
    public static FG_CrowdFunding newInstance() {
        FG_CrowdFunding fragment = new FG_CrowdFunding();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_crowdfunding, container, false);
        //热门推荐的listview
        ListView lv_PR=(ListView)view.findViewById(R.id.lv_PopularRecommendation);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("iv_PJImage",R.drawable.pjimagetest);
            tempMap.put("tv_PJIntro","这是一个简介");
            tempMap.put("tv_Finance",getResources().getString(R.string.finance)+"10000");
            tempMap.put("tv_SupportNum",getResources().getString(R.string.supportNum)+"10000");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getContext(),listItems,R.layout.item_project,
                new String[]{"tv_PJName","iv_PJImage","tv_PJIntro","tv_Finance","tv_SupportNum"},
                new int[]{R.id.tv_PJ_Name,R.id.iv_PJ_Image,R.id.tv_PJ_Intro,R.id.tv_PJ_Finance,R.id.tv_PJ_SupportNum});


        lv_PR.setAdapter(simpleAdapter);

        //将其他view作为热门推荐listview的头部view
        View listHeader=inflater.inflate(R.layout.fragment_crowdfundingheader, container, false);
        lv_PR.addHeaderView(listHeader);


        //设置按钮跳转
        int[] btn_id={R.id.btn_cosmetic,R.id.btn_food,R.id.btn_game,R.id.btn_literature};
        for(int id:btn_id){
            Button button=(Button)listHeader.findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button=(Button)v;
                    Intent intent = new Intent();
                    //用来设置新activity的actionbar的title部
                    intent.putExtra(getResources().getString(R.string.category_actionbar_title),button.getText());
                    intent.setAction(getResources().getString(R.string.activity_category));
                    startActivity(intent);
                }
            });
        }
        return view;
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
