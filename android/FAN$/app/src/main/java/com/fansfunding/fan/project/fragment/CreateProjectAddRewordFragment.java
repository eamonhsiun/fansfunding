package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fansfunding.fan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProjectAddRewordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateProjectAddRewordFragment extends Fragment{

    AppCompatEditText et_support_money;
    AppCompatEditText et_reward_content;

    public String getSupportMoney(){
        String et=  et_support_money.getText().toString();
        et_support_money.setText("");
        return et;
    }

    public String getRewardContent(){

        String et=  et_reward_content.getText().toString();
        et_reward_content.setText("");
        return et;
    }

    public static CreateProjectAddRewordFragment createProjectAddRewordFragment;

    private SimpleAdapter imageAdapter;

    private List<Map<String,Object>> bitmapItems=new ArrayList<>();



    public CreateProjectAddRewordFragment() {
        // Required empty public constructor
    }

    public void addToList(Bitmap bitmap){

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProjectAddRewordFragment newInstance() {
        CreateProjectAddRewordFragment fragment = new CreateProjectAddRewordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        createProjectAddRewordFragment=fragment;
        return fragment;
    }

    public static CreateProjectAddRewordFragment getInstance(){
        return createProjectAddRewordFragment;
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
        View rootView=inflater.inflate(R.layout.fragment_create_project_reword_add, container, false);

        et_support_money = (AppCompatEditText) rootView.findViewById(R.id.et_support_money);
        et_reward_content = (AppCompatEditText) rootView.findViewById(R.id.et_reward_content);

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
