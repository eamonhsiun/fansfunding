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

import com.fansfunding.internal.AllProjectInCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailDynamicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailDynamicFragment extends Fragment {

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;
    //项目描述信息(比如目标金额之类的)
    private AllProjectInCategory.ProjectDetail projectDetail;

    public ProjectDetailDynamicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProjectDetailDynamicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailDynamicFragment newInstance (AllProjectInCategory.ProjectDetail projectDetail){
        ProjectDetailDynamicFragment fragment = new ProjectDetailDynamicFragment();
        Bundle args = new Bundle();
        args.putSerializable("projectDetail",projectDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.projectDetail= (AllProjectInCategory.ProjectDetail) getArguments().getSerializable("projectDetail");
            categoryId=projectDetail.getCategoryId();
            projectId=projectDetail.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_project_detail_dynamic, container, false);
        ListView lv_project_detail_reward=(ListView)rootView.findViewById(R.id.lv_project_detail_dynamic);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<3;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this.getContext(),listItems,R.layout.item_project_detail_dynamic,
                new String[]{},
                new int[]{});
        lv_project_detail_reward.setAdapter(simpleAdapter);
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
