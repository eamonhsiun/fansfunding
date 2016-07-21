package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.utils.AddImageListener;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateProjectFragment extends Fragment {

    public static  CreateProjectFragment createProjectFragment;

    public  static CreateProjectFragment getInstance(){
        return createProjectFragment;
    }


    public AppCompatTextView tv_project_create_time;

    public AppCompatImageButton btn_project_image;

    public AppCompatEditText et_target_money;

    public AppCompatEditText et_project_title;

    public AppCompatEditText et_project_desc;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==tv_project_create_time.getId()){
                DatePicker picker = new DatePicker(CreateProjectFragment.this.getActivity(), DatePicker.YEAR_MONTH_DAY);
                picker.setRange(2016, 2026);//年份范围
                Calendar c = Calendar.getInstance();
                picker.setSelectedItem(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {tv_project_create_time.setText(year+"-"+month+"-"+day);}
                });
                picker.show();
            }
        }
    };



    public CreateProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProjectFragment newInstance() {
        CreateProjectFragment fragment = new CreateProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        createProjectFragment=fragment;
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
        View rootView=inflater.inflate(R.layout.fragment_create_project, container, false);
        tv_project_create_time = (AppCompatTextView)rootView.findViewById(R.id.tv_project_create_time);
        tv_project_create_time.setOnClickListener(clickListener);
        Calendar c = Calendar.getInstance();
        tv_project_create_time.setText(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));

        btn_project_image= (AppCompatImageButton) rootView.findViewById(R.id.btn_project_image);
        btn_project_image.setOnClickListener(new AddImageListener(this.getActivity(),"选择项目介绍图片"));

        et_target_money= (AppCompatEditText) rootView.findViewById(R.id.et_target_money);

        et_project_title= (AppCompatEditText) rootView.findViewById(R.id.et_project_title);

        et_project_desc= (AppCompatEditText) rootView.findViewById(R.id.et_project_desc);

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
