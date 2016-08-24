package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.fansfunding.PullListView.LoadListView;
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
 * Use the {@link MomentPraiseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentPraiseFragment extends Fragment implements ScrollableHelper.ScrollableContainer,MomentActivity.OnLoadListViewReset {


    private LoadListView lv_moment_praise;

    public MomentPraiseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MomentPraiseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentPraiseFragment newInstance() {
        MomentPraiseFragment fragment = new MomentPraiseFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_moment_praise, container, false);
        lv_moment_praise=(LoadListView) rootView.findViewById(R.id.lv_moment_praise);
        lv_moment_praise.setPullLoadEnable(false);
        lv_moment_praise.setAutoLoadEnable(false);
        lv_moment_praise.setRefreshTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<10;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();

            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getActivity(),listItems,R.layout.item_moment_praise,
                new String[]{},
                new int[]{});
        lv_moment_praise.setAdapter(simpleAdapter);
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
    public void resetLoadListView() {
        if(lv_moment_praise!=null&&lv_moment_praise.isAtTop()==false){
            lv_moment_praise.setSelection(0);
        }
    }

    @Override
    public View getScrollableView() {

        return lv_moment_praise;
    }
}
