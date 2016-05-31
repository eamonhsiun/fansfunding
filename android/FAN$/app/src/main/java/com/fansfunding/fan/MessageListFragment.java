package com.fansfunding.fan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * 用来显示消息界面中的消息列表
 */
public class MessageListFragment extends Fragment {

    public MessageListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MessageListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_messagelist, container, false);
        TextView tv_unread=(TextView)rootView.findViewById(R.id.tv_msg_unread);

        ListView lv_msg_list=(ListView)rootView.findViewById(R.id.lv_msg_list);

        //构建simpleAdapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            tempMap.put("msg_picture",R.drawable.pjimagetest);
            tempMap.put("msg_name","名字");
            tempMap.put("msg_content","这是一段内容");
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getContext(),listItems,R.layout.item_message,
                new String[]{"msg_picture","msg_name","msg_content"},
                new int[]{R.id.iv_msg,R.id.tv_msg_name,R.id.tv_msg_content});

        lv_msg_list.setAdapter(simpleAdapter);

        lv_msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_chat));
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
