package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fansfunding.fan.project.adapter.ProjectDetailAdapter;
import com.fansfunding.fan.R;
import com.fansfunding.internal.ProjectInfo;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailViewPaperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailViewPaperFragment extends Fragment {

    public ProjectDetailViewPaperFragment() {
        // Required empty public constructor
    }

    //项目分类
    private int categoryId;

    //项目Id
    private int projectId;


    private ViewPager vp_project_detail;
    private ProjectDetailAdapter adapter;
    private TabLayout tabLayout;

    //项目描述信息(比如目标金额之类的)
    private ProjectInfo projectDetail;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment P0rojectDetailViewPaperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailViewPaperFragment newInstance(ProjectInfo projectDetail) {
        ProjectDetailViewPaperFragment fragment = new ProjectDetailViewPaperFragment();
        Bundle args = new Bundle();
        args.putSerializable("projectDetail",projectDetail);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.projectDetail= (ProjectInfo) getArguments().getSerializable("projectDetail");
            categoryId=projectDetail.getCategoryId();
            projectId=projectDetail.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_project_detail_view_paper, container, false);

        adapter=new ProjectDetailAdapter(getActivity().getSupportFragmentManager(),projectDetail);

        vp_project_detail=(ViewPager)rootView.findViewById(R.id.vp_project_detail) ;
        vp_project_detail.setAdapter(adapter);
        vp_project_detail.setOffscreenPageLimit(3);

        tabLayout=(TabLayout)rootView.findViewById(R.id.tab_project_detail);

        tabLayout.setupWithViewPager(vp_project_detail);
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
