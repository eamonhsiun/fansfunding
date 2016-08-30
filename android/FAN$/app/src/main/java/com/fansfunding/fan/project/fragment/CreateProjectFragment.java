package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.project.activity.CreateProjectActivity;
import com.fansfunding.fan.project.adapter.ChoosePhotoListAdapter;
import com.fansfunding.fan.project.listener.ChooseImageListener;
import com.fansfunding.fan.project.utils.DatePicker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.HorizontalListView;


public class CreateProjectFragment extends Fragment {

    public static CreateProjectFragment createProjectFragment;
    //Views
    private AppCompatTextView tvProjectCreateTime;
    private AppCompatEditText etTargetMoney;
    private AppCompatEditText etProjectTitle;
    private AppCompatEditText etProjectDesc;
    //Constans
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    private List<PhotoInfo> mPhotoList;
    private ChoosePhotoListAdapter mChoosePhotoListAdapter;
    private FloatingActionButton mOpenGallery;
    private HorizontalListView mLvPhoto;

    public CreateProjectFragment() {}
    public static CreateProjectFragment getInstance(){
        return createProjectFragment;
    }
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
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==tvProjectCreateTime.getId()){
                startPicker();
            }
        }
    };

    private void startPicker() {
        DatePicker picker = new DatePicker(CreateProjectFragment.this.getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setRange(2015, 2050);//年份范围
        Calendar c = Calendar.getInstance();
        picker.setSelectedItem(c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH)+1);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                try{
                    int sYear=Integer.parseInt(year);
                    int sMonth=Integer.parseInt(month);
                    int sDay=Integer.parseInt(day);
                    Calendar c= Calendar.getInstance();
                    int nYear=c.get(Calendar.YEAR);
                    int nMonth=c.get(Calendar.MONTH)+1;
                    int nDay=c.get(Calendar.DAY_OF_MONTH)+1;
                    if(!((sYear>=nYear)&&(sMonth>=nMonth)&&(sDay>nDay))){
                        throw new Exception();
                    }
                    tvProjectCreateTime.setText(sYear+"-"+sMonth+"-"+sDay);
                }catch (Exception e){
                    Toast.makeText(CreateProjectFragment.this.getActivity(),"时间不合理呦亲",Toast.LENGTH_LONG).show();
                }

            }
        });
        picker.show();
    }

    private void initBaseView(View rootView) {
        initTime(rootView);
        initEditText(rootView);
    }

    private void initEditText(View rootView) {
        etTargetMoney= (AppCompatEditText) rootView.findViewById(R.id.et_target_money);
        etProjectTitle= (AppCompatEditText) rootView.findViewById(R.id.et_project_title);
        etProjectDesc= (AppCompatEditText) rootView.findViewById(R.id.et_project_desc);
    }

    private void initTime(View rootView) {
        tvProjectCreateTime = (AppCompatTextView)rootView.findViewById(R.id.tv_project_create_time);
        tvProjectCreateTime.setOnClickListener(clickListener);
        Calendar c = Calendar.getInstance();
        tvProjectCreateTime.setText(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+(c.get(Calendar.DAY_OF_MONTH)+1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_create_project, container, false);
        initBaseView(rootView);
        mLvPhoto=(HorizontalListView)rootView.findViewById(R.id.lv_imageView);
        mPhotoList = new ArrayList<>();
        mChoosePhotoListAdapter = new ChoosePhotoListAdapter(this.getActivity(), mPhotoList);
        mLvPhoto.setAdapter(mChoosePhotoListAdapter);

        mOpenGallery = (FloatingActionButton) rootView.findViewById(R.id.btn_open_gallery);
        mOpenGallery.setOnClickListener(new ChooseImageListener(CreateProjectActivity.getInstance(),mOnHanlderResultCallback,mPhotoList));

        initImageLoader(CreateProjectFragment.this.getActivity());

        return rootView;
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

    public List<PhotoInfo> getAndClearPhotoList() {
        List<PhotoInfo> newList=new ArrayList<>();
        newList.addAll(mPhotoList);
        mPhotoList.clear();
        return newList;
    }

    public List<PhotoInfo> getPhotoList() {
        List<PhotoInfo> newList =mPhotoList;
        return newList;
    }

    public int getEtTargetMoney() {
        if(etTargetMoney.getText().toString().equals(""))
           return 0;
        else {
            return (int)Double.parseDouble(etTargetMoney.getText().toString());
        }

    }

    public String getEtProjectTitle() {
        return etProjectTitle.getText().toString();
    }


    public String getEtProjectDesc() {
        return etProjectDesc.getText().toString();
    }

    public String getTvTargetTime(){
        return tvProjectCreateTime.getText().toString();
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                if(mPhotoList.size()==0){
                    mPhotoList.addAll(resultList);
                }else{
                    for(PhotoInfo result:resultList){
                        for(int i=0;i<mPhotoList.size();i++){
                            if(result.getPhotoPath().equals(mPhotoList.get(i).getPhotoPath())){
                                break;
                            }
                            if(i==mPhotoList.size()-1){
                                mPhotoList.add(result);
                            }
                        }
                    }
                }
                if(resultList.size()==0){
                    mPhotoList.clear();
                }
                mPhotoList.addAll(resultList);
                mChoosePhotoListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(CreateProjectFragment.this.getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

}
