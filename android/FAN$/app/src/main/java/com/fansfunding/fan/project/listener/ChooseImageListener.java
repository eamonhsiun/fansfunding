package com.fansfunding.fan.project.listener;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.utils.PicassoImageLoader;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Eamon on 2016/7/22.
 */
public class ChooseImageListener implements View.OnClickListener {
    //Constans
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback;
    private AppCompatActivity activity;
    private List<PhotoInfo> mPhotoList;

    public ChooseImageListener(AppCompatActivity activity, GalleryFinal.OnHanlderResultCallback resultCallback, List<PhotoInfo> mPhotoList) {
        this.activity = activity;
        this.mPhotoList = mPhotoList;
        this.mOnHanlderResultCallback = resultCallback;
    }

    @Override
    public void onClick(View v) {

        ThemeConfig themeConfig = null;


        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(activity.getResources().getColor(R.color.colorPrimary))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(activity.getResources().getColor(R.color.colorPrimary))
                .setFabPressedColor(activity.getResources().getColor(R.color.colorPrimary))
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(R.color.title_color)
                .setIconBack(R.drawable.ic_gf_back)
                .setIconRotate(R.drawable.ic_gf_rotate)
                .setIconCrop(R.drawable.ic_gf_crop)
                .setIconCamera(R.drawable.ic_gf_camera)
                .build();
        themeConfig = theme;


        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;


        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);


        boolean muti = false;

        muti = true;
        int maxSize = 9;
        functionConfigBuilder.setMutiSelectMaxSize(maxSize);

        final boolean mutiSelect = muti;

        functionConfigBuilder.setEnableEdit(true);
        functionConfigBuilder.setEnableRotate(true);
        functionConfigBuilder.setRotateReplaceSource(true);


        functionConfigBuilder.setCropSquare(true);


        functionConfigBuilder.setEnableCamera(true);

        functionConfigBuilder.setEnablePreview(true);


        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合

        final FunctionConfig functionConfig = functionConfigBuilder.build();

        CoreConfig coreConfig = new CoreConfig.Builder(activity, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);



        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }
}






