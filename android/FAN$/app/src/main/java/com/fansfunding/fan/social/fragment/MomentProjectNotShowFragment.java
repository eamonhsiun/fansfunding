package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fansfunding.fan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MomentProjectNotShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentProjectNotShowFragment extends Fragment {

    public MomentProjectNotShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MomentProjectNotShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentProjectNotShowFragment newInstance() {
        MomentProjectNotShowFragment fragment = new MomentProjectNotShowFragment();
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
        return inflater.inflate(R.layout.fragment_moment_project_not_show, container, false);
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
