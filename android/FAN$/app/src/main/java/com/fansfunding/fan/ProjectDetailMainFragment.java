package com.fansfunding.fan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailMainFragment extends Fragment {


    public ProjectDetailMainFragment(){

    }
    /**
     * Use this factory method to create a new instance of
     * @return A new instance of fragment ProjectDetailMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailMainFragment newInstance() {
        ProjectDetailMainFragment fragment = new ProjectDetailMainFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_project_detail_main, container, false);


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


