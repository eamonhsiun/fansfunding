package com.fansfunding.fan.social.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.fansfunding.fan.R;
import com.fansfunding.fan.social.adapter.MomentPaperAdapter;
import com.fansfunding.fan.social.fragment.MomentCommentFragment;
import com.fansfunding.fan.social.fragment.MomentPraiseFragment;
import com.github.florent37.materialviewpager.MaterialViewPager;

import java.util.ArrayList;

public class MomentActivity extends AppCompatActivity{

    protected ViewPager vp_moment;

    protected TabLayout tab_moment;

    protected ScrollableLayout sl_moment;

    protected int momentId;

    protected MomentPaperAdapter momentPaperAdapter;

    protected ArrayList<ScrollableHelper.ScrollableContainer> fragmentList;

    //是否需要将列表置顶
    private boolean isNeedResetLoadListView=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        initVariables();
        initViews();
        loadData();
    }

    protected void initVariables(){
        //初始化adapter
        fragmentList=new ArrayList<ScrollableHelper.ScrollableContainer>();
        ScrollableHelper.ScrollableContainer momentComment=MomentCommentFragment.newInstance();
        ScrollableHelper.ScrollableContainer momentCommentTemp= MomentPraiseFragment.newInstance();
        fragmentList.add(momentComment);
        fragmentList.add(momentCommentTemp);
        momentPaperAdapter=new MomentPaperAdapter(getSupportFragmentManager(),fragmentList);

        //获取动态相关信息
        Intent intent=getIntent();

    }

    protected void initViews(){

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_moment);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("动态");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        vp_moment=(ViewPager)findViewById(R.id.vp_moment);
        vp_moment.setAdapter(momentPaperAdapter);
        vp_moment.setOffscreenPageLimit(3);
        vp_moment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置当前展示的listview所在的fragment
                sl_moment.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tab_moment=(TabLayout)findViewById(R.id.tab_moment);
        tab_moment.setupWithViewPager(vp_moment);

        sl_moment=(ScrollableLayout)findViewById(R.id.sl_moment);
        sl_moment.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        sl_moment.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {

                //将listview设为置顶
                if(isNeedResetLoadListView==true){
                    for(int i=0;i<fragmentList.size();i++){
                        if(fragmentList.get(i) instanceof MomentActivity.OnLoadListViewReset){
                            ((OnLoadListViewReset) fragmentList.get(i)).resetLoadListView();
                        }
                    }
                    isNeedResetLoadListView=false;
                }
                if(currentY==maxY){
                    isNeedResetLoadListView=true;
                }
            }
        });
    }

    protected void loadData(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    public interface OnLoadListViewReset {
        // TODO: Update argument type and name
        void resetLoadListView();
    }

}
