package com.fansfunding.fan;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fansfunding.PullListView.XListView;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrowdFundingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来创建众筹界面
 */
public class CrowdFundingFragment extends Fragment {


    //热门项目列表
    private XListView lv_PR;


    public CrowdFundingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CrowdFundingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrowdFundingFragment newInstance() {
        CrowdFundingFragment fragment = new CrowdFundingFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_crowdfunding, container, false);
        //热门推荐的listview
        lv_PR=(XListView)rootView.findViewById(R.id.lv_popularRecommendation);
        lv_PR.setAutoLoadEnable(true);
        lv_PR.setPullLoadEnable(true);
        lv_PR.setPullRefreshEnable(true);
        lv_PR.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        lv_PR.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(CrowdFundingFragment.this.getActivity(),"下拉更新",Toast.LENGTH_LONG).show();
                lv_PR.stopRefresh();
                lv_PR.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(CrowdFundingFragment.this.getActivity(),"上啦加载",Toast.LENGTH_LONG).show();
                lv_PR.stopRefresh();
                lv_PR.stopLoadMore();
                lv_PR.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        });


        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("tv_PJName","项目名称");
            tempMap.put("tv_PJIntro","这是一个简介这是一个简介这是一个简介这是一个简介这是一个简介这是一个简介");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getContext(),listItems,R.layout.item_project,
                new String[]{"tv_PJName","tv_PJIntro"},
                new int[]{R.id.tv_PJ_name,R.id.tv_PJ_intro});


        View listHeader=inflater.inflate(R.layout.fragment_crowdfundingheader, null, false);

        //将其他view作为热门推荐listview的头部view
        /*
        * 先暂时取消banner和分类，以后再加上去
        * */
        //lv_PR.addHeaderView(listHeader,null,false);


        lv_PR.setAdapter(simpleAdapter);



        View view_cosmetic=listHeader.findViewById(R.id.ll_cosmetic);
        view_cosmetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"美妆");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_food=listHeader.findViewById(R.id.ll_food);
        view_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"美食");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_game=listHeader.findViewById(R.id.ll_game);
        view_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"游戏");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        View view_literature=listHeader.findViewById(R.id.ll_literature);
        view_literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //用来设置新activity的actionbar的title部
                intent.putExtra(getResources().getString(R.string.actionbar_title),"文学");
                intent.setAction(getResources().getString(R.string.activity_category));
                startActivity(intent);
            }
        });

        lv_PR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(getString(R.string.activity_project_detail));
                startActivity(intent);
            }
        });

        Toolbar toolbar =(Toolbar)rootView.findViewById(R.id.toolbar_crowdfundinghead);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_search));
                startActivity(intent);
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
