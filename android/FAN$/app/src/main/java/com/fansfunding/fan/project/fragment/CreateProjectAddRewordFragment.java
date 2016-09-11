package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.listener.ChooseImageListener;
import com.fansfunding.fan.project.activity.CreateProjectActivity;
import com.fansfunding.fan.project.adapter.ChoosePhotoListAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProjectAddRewordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateProjectAddRewordFragment extends Fragment{

    private AppCompatEditText etSupportMoney;
    private AppCompatEditText etRewardContent;
    private AppCompatEditText etRewardCeiling;
    private SwitchCompat swOpenCeiling;
    private LinearLayout llCeiling;
    private int ceiling;

    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private FloatingActionButton mOpenGallery;
    private HorizontalListView mLvPhoto;

    //Constans
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    public List<PhotoInfo> getAndClearPhotoList() {
        List<PhotoInfo> newPhotoList = new ArrayList<>();
        newPhotoList.addAll(mPhotoList);
        mPhotoList.clear();
        mChoosePhotoListAdapter.notifyDataSetChanged();
        return newPhotoList;
    }

    public String getSupportMoney(){
        String et=  etSupportMoney.getText().toString();

        return et;
    }

    public String getRewardContent(){

        String et=  etRewardContent.getText().toString();

        return et;
    }

    public int getCeiling(){
        String et = etRewardCeiling.getText().toString();
        if(ceiling>=0){
            if(!et.equals("")){
                ceiling=Integer.parseInt(et);
            }
        }

        return ceiling;
    }


    public static CreateProjectAddRewordFragment createProjectAddRewordFragment;

    public CreateProjectAddRewordFragment() {
        // Required empty public constructor
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

        etSupportMoney = (AppCompatEditText) rootView.findViewById(R.id.et_support_money);
        etRewardContent = (AppCompatEditText) rootView.findViewById(R.id.et_reward_content);
        etRewardCeiling=(AppCompatEditText) rootView.findViewById(R.id.et_create_project_reword_ceiling);
        swOpenCeiling =(SwitchCompat)rootView.findViewById(R.id.sw_create_project_reword_ceiling_open);
        llCeiling=(LinearLayout)rootView.findViewById(R.id.ll_create_project_reword_ceiling);
        llCeiling.setVisibility(View.INVISIBLE);


        swOpenCeiling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    llCeiling.setVisibility(View.VISIBLE);
                    ceiling=0;
                }else{
                    llCeiling.setVisibility(View.INVISIBLE);
                    ceiling=-1;
                }
            }
        });


        mLvPhoto=(HorizontalListView)rootView.findViewById(R.id.lv_imageView);
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this.getActivity(), mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);

        mOpenGallery = (FloatingActionButton) rootView.findViewById(R.id.btn_open_gallery);
        mOpenGallery.setOnClickListener(new ChooseImageListener(CreateProjectActivity.getInstance(),mOnHanlderResultCallback,mPhotoList));

        initImageLoader(CreateProjectAddRewordFragment.this.getActivity());


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

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            switch (reqeustCode) {
                case REQUEST_CODE_CAMERA:
                    Log.e("TEST", "REQUEST_CODE_CAMERA " + resultList.size());

                    break;
                case REQUEST_CODE_GALLERY:
                    Log.e("TEST", "REQUEST_CODE_GALLERY " + resultList.size());
                    break;
            }

            if (resultList != null) {
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(CreateProjectAddRewordFragment.this.getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public List<PhotoInfo> getPhotoList() {
        List<PhotoInfo> newPhotoList = new ArrayList<>();
        newPhotoList.addAll(mPhotoList);
        return newPhotoList;
    }

    public void ClearSupportMoney() {
        etSupportMoney.setText("");
    }

    public void ClearRewardContent() {
        etRewardContent.setText("");
    }
}
