package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.fansfunding.PullListView.LoadListView;
import com.fansfunding.PullListView.XListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.social.activity.MomentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MomentCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentCommentFragment extends Fragment implements ScrollableHelper.ScrollableContainer,MomentActivity.OnLoadListViewReset{

    private LoadListView lv_moment_comment;


    public MomentCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MomentCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentCommentFragment newInstance() {
        MomentCommentFragment fragment = new MomentCommentFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_moment_comment, container, false);
        lv_moment_comment=(LoadListView) rootView.findViewById(R.id.lv_moment_comment);
        lv_moment_comment.setPullLoadEnable(false);
        lv_moment_comment.setAutoLoadEnable(false);
        lv_moment_comment.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<10;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();

            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_comment,
                new String[]{},
                new int[]{});
        lv_moment_comment.setAdapter(simpleAdapter);
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


    @Override
    public View getScrollableView() {
        return lv_moment_comment;
    }

    //将listview置为顶部
    @Override
    public void resetLoadListView() {
        if(lv_moment_comment!=null&&lv_moment_comment.isAtTop()==false){
            lv_moment_comment.setSelection(0);
        }
    }
}
