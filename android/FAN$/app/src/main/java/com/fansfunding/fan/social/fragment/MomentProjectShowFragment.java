package com.fansfunding.fan.social.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fansfunding.fan.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MomentProjectShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentProjectShowFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PROJECT_COVER = "PROJECT_COVER";
    private static final String PROJECT_NAME = "PROJECT_NAME";

    //项目封面
    private String projectCover;

    //项目名称
    private String projectName;


    public MomentProjectShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MomentProjectShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MomentProjectShowFragment newInstance(String projectCover,String projectName) {
        MomentProjectShowFragment fragment = new MomentProjectShowFragment();
        Bundle args = new Bundle();
        args.putString(PROJECT_COVER,projectCover);
        args.putString(PROJECT_NAME,projectName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectCover=getArguments().getString(PROJECT_COVER);
            projectName=getArguments().getString(PROJECT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_moment_project_show, container, false);
        ImageView iv_social_project_cover=(ImageView)rootView.findViewById(R.id.iv_social_project_cover);
        if(projectCover!=null&&projectCover.equals("")==false){
            Picasso.with(getActivity())
                    .load(getString(R.string.url_resources)+projectCover)
                    .resize(70,70).centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(iv_social_project_cover);
        }

        TextView tv_social_project_name=(TextView)rootView.findViewById(R.id.tv_social_project_name);
        tv_social_project_name.setText(projectName);
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
