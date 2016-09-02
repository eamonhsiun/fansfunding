package com.fansfunding.fan.social.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.fansfunding.fan.R;
import com.fansfunding.fan.login.LoginActivity;
import com.fansfunding.fan.project.activity.ProjectCommentActivity;
import com.fansfunding.fan.request.RequestPraiseMoment;
import com.fansfunding.fan.request.RequestSingleMoment;
import com.fansfunding.fan.social.adapter.MomentPaperAdapter;
import com.fansfunding.fan.social.adapter.SocialMomentPhotoAdapter;
import com.fansfunding.fan.social.fragment.MomentCommentFragment;
import com.fansfunding.fan.social.fragment.MomentPraiseFragment;
import com.fansfunding.fan.social.interfacetest.IInitNum;
import com.fansfunding.fan.utils.DefaultValue;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.fan.utils.LoginSituation;
import com.fansfunding.fan.utils.MyGridView;
import com.fansfunding.fan.utils.StartHomepage;
import com.fansfunding.internal.social.SingleUserMoment;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.nostra13.universalimageloader.utils.L;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MomentActivity extends AppCompatActivity implements IInitNum{

    public static final String MOMENTID="MOMENTID";


    public static final int RESET_COLUMN_NUM=1;

    public static final int RESET_PRAISE_NUM=2;

    private boolean isFinishRequest=true;

    protected ViewPager vp_moment;

    protected TabLayout tab_moment;

    protected ScrollableLayout sl_moment;


    //用户id
    private int userId;

    //用户token
    private String token;

    //动态发起人头像
    private CircleImageView iv_social_publisher_head;

    //动态发起人昵称
    private TextView tv_social_publisher_nickname;

    //动态发起时间
    private TextView tv_social_publish_time;

    //动态内容
    private  EmojiconTextView tv_social_publisher_content;

    //动态图片展示区
    private  MyGridView gv_social_photos;

    //动态所指项目所在的布局
    private LinearLayout ll_social_project;

    //动态展示的项目的封面
    private ImageView iv_social_project_cover;

    //动态展示的项目的名称
    private TextView tv_social_project_name;

    //评论按钮
    private LinearLayout ll_moment_send_comment;

    //赞按钮
    private LinearLayout ll_moment_praise;

    //赞的展示内容
    private TextView tv_moment_praise_text;

    //赞的图标
    private ImageView iv_moment_praise;

    //动态id
    private int momentId;

    //请求动态详情
    private RequestSingleMoment requestSingleMoment;

    private OkHttpClient httpClient;



    protected MomentPaperAdapter momentPaperAdapter;

    protected ArrayList<ScrollableHelper.ScrollableContainer> fragmentList;

    //是否需要将列表置顶
    private boolean isNeedResetLoadListView=false;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_SINGLE_USER_MOMENT_SUCCESS:
                    isFinishRequest=true;
                    setMomentView();
                    break;
                case FANRequestCode.GET_SINGLE_USER_MOMENT_FAILURE:
                    if(MomentActivity.this.isFinishing()==false){
                        Toast.makeText(MomentActivity.this,"获取动态详情失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.PRAISE_MOMENT_SUCCESS:
                    isFinishRequest=true;
                    //设置是否赞了该动态
                    if(requestSingleMoment.getSingleUserMoment().getData().isIsLike()==false){
                        //requestSingleMoment.getSingleUserMoment().getData().setLikeNum(requestSingleMoment.getSingleUserMoment().getData().getLikeNum()+1);
                    }
                    requestSingleMoment.getSingleUserMoment().getData().setIsLike(true);
                    tv_moment_praise_text.setText("已赞");
                    iv_moment_praise.setImageResource(R.drawable.moment_praise_pressed);
                    //tab_moment.getTabAt(1).setText("赞("+requestSingleMoment.getSingleUserMoment().getData().getLikeNum()+")");
                    ((MomentPraiseFragment)fragmentList.get(1)).refreshPraiseList();

                    break;
                case FANRequestCode.PRAISE_MOMENT_FAILURE:
                    isFinishRequest=true;
                    if(MomentActivity.this.isFinishing()==false){
                        Toast.makeText(MomentActivity.this,"点赞失败,请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case FANRequestCode.CANCEL_PRAISE_MOMENT_SUCCESS:
                    isFinishRequest=true;
                    //设置是否赞了该动态
                    if(requestSingleMoment.getSingleUserMoment().getData().isIsLike()==true){
                        //requestSingleMoment.getSingleUserMoment().getData().setLikeNum(requestSingleMoment.getSingleUserMoment().getData().getLikeNum()-1);
                    }
                    requestSingleMoment.getSingleUserMoment().getData().setIsLike(false);
                    tv_moment_praise_text.setText("赞");
                    iv_moment_praise.setImageResource(R.drawable.moment_praise);
                    //tab_moment.getTabAt(1).setText("赞("+requestSingleMoment.getSingleUserMoment().getData().getLikeNum()+")");
                    ((MomentPraiseFragment)fragmentList.get(1)).refreshPraiseList();
                    break;
                case FANRequestCode.CANCEL_PRAISE_MOMENT_FAILURE:
                    isFinishRequest=true;
                    if(MomentActivity.this.isFinishing()==false){
                        Toast.makeText(MomentActivity.this,"取消赞失败,请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        initVariables();
        initViews();
        loadData();
    }

    protected void initVariables(){
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        userId=share.getInt("id",-1);
        token=share.getString("token"," ");

        if(userId<0|| LoginSituation.isLogin(this)==false){
            userId= DefaultValue.DEFAULT_USERID;
        }

        //获取动态相关信息
        Intent intent=getIntent();
        momentId=intent.getIntExtra(MOMENTID,-1);
        httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestSingleMoment=new RequestSingleMoment();

        //初始化adapter
        fragmentList=new ArrayList<ScrollableHelper.ScrollableContainer>();
        ScrollableHelper.ScrollableContainer momentComment=MomentCommentFragment.newInstance(momentId);
        ScrollableHelper.ScrollableContainer momentCommentTemp= MomentPraiseFragment.newInstance(momentId);
        fragmentList.add(momentComment);
        fragmentList.add(momentCommentTemp);
        momentPaperAdapter=new MomentPaperAdapter(getSupportFragmentManager(),fragmentList);

    }

    protected void initViews(){

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_moment);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("动态");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);

        tv_moment_praise_text=(TextView)findViewById(R.id.tv_moment_praise_text);

        iv_social_publisher_head=(CircleImageView)findViewById(R.id.iv_social_publisher_head);
        tv_social_publisher_nickname=(TextView)findViewById(R.id.tv_social_publisher_nickname);
        tv_social_publish_time=(TextView)findViewById(R.id.tv_social_publish_time) ;
        tv_social_publisher_content=(EmojiconTextView)findViewById(R.id.tv_social_publisher_content);

        gv_social_photos=(MyGridView)findViewById(R.id.gv_social_photos);

        ll_social_project=(LinearLayout)findViewById(R.id.ll_social_project);
        iv_social_project_cover=(ImageView)findViewById(R.id.iv_social_project_cover);
        tv_social_project_name=(TextView)findViewById(R.id.tv_social_project_name);

        ll_moment_send_comment=(LinearLayout)findViewById(R.id.ll_moment_send_comment);
        ll_moment_praise=(LinearLayout)findViewById(R.id.ll_moment_praise);
        iv_moment_praise=(ImageView)findViewById(R.id.iv_moment_praise);
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

        ll_moment_send_comment=(LinearLayout)findViewById(R.id.ll_moment_send_comment);

        ll_moment_praise=(LinearLayout)findViewById(R.id.ll_moment_praise);

    }

    protected void loadData(){
        requestSingleMoment.requestSingleMoment(this,handler,httpClient,momentId,userId,userId);
    }

    private void setMomentView(){
        if(requestSingleMoment.getSingleUserMoment()==null) {
            return;
        }
        final SingleUserMoment.DataBean moment=requestSingleMoment.getSingleUserMoment().getData();
        if(moment==null){
            return;
        }
        //设置动态发起人昵称
        tv_social_publisher_nickname.setText(moment.getUser().getNickname());
        tv_social_publisher_nickname.setOnClickListener(new StartHomepage(this,requestSingleMoment.getSingleUserMoment().getData().getUser().getId()));
        //设置动态发送时间
        tv_social_publish_time.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(moment.getPostTime())));
        //设置动态内容
        tv_social_publisher_content.setText(requestSingleMoment.getSingleUserMoment().getData().getContent());
        //设置动态发起人头像
        if(moment.getUser().getHead()!=null&&moment.getUser().getHead().equals("")==false){
            Picasso.with(this).load(getString(R.string.url_resources)+moment.getUser().getHead()).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_social_publisher_head);
        }
        iv_social_publisher_head.setOnClickListener(new StartHomepage(this,requestSingleMoment.getSingleUserMoment().getData().getUser().getId()));
        //设置动态图片
        if(moment.getImages()!=null){
            SocialMomentPhotoAdapter adapter=new SocialMomentPhotoAdapter(this);
            for(int i=0;i<moment.getImages().size();i++){
                if(moment.getImages().get(i)!=null&&moment.getImages().get(i).equals("")==false){
                    adapter.addItem(moment.getImages().get(i));
                }
            }
            gv_social_photos.setAdapter(adapter);
            if(adapter.getCount()>1){
                gv_social_photos.setNumColumns(3);
            }else {
                gv_social_photos.setNumColumns(1);
            }
            adapter.notifyDataSetChanged();
        }
        //设置动态附带项目内容
        if(moment.getProject()==null){
            ll_social_project.setVisibility(View.GONE);
        }else {
            Picasso.with(this).load(getString(R.string.url_resources)+moment.getProject().getCover()).resize(70,70).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_social_project_cover);
            tv_social_project_name.setText(moment.getProject().getName());
            ll_social_project.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction(getString(R.string.activity_project_detail));
                    intent.putExtra("projectId",moment.getProject().getId());
                    intent.putExtra("categoryId",moment.getProject().getCategoryId());
                    startActivity(intent);
                }
            });
        }

        //设置评论按钮点击响应函数
        ll_moment_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginSituation.isLogin(MomentActivity.this)==false){
                    LoginActivity.loginForResult(MomentActivity.this,LoginActivity.REQUEST_LOGIN_BY_PHONE);
                    return;
                }
                //如果尚未返回动态信息，则直接返回
                if(requestSingleMoment.getSingleUserMoment()==null){
                    return;
                }
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project_comment));
                intent.putExtra("momentId",momentId);
                intent.putExtra("mode", ProjectCommentActivity.SEND_MOMENT_COMMENT);
                startActivityForResult(intent,ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY);
            }
        });

        //设置赞按钮点击响应函数
        ll_moment_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginSituation.isLogin(MomentActivity.this)==false){
                    LoginActivity.loginForResult(MomentActivity.this,LoginActivity.REQUEST_LOGIN_BY_PHONE);
                    return;
                }
                //如果尚未返回动态信息，则直接返回
                if(requestSingleMoment.getSingleUserMoment()==null){
                    return;
                }
                if(isFinishRequest==false){
                    Toast.makeText(MomentActivity.this,"正在处理，请稍后",Toast.LENGTH_SHORT).show();
                    return;
                }
                isFinishRequest=false;
                if(requestSingleMoment.getSingleUserMoment().getData().isIsLike()==false){
                    RequestPraiseMoment.requestPraiseMoment(MomentActivity.this,handler,httpClient,momentId,userId,token);
                }else {
                    RequestPraiseMoment.requestCancelPraiseMoment(MomentActivity.this,handler,httpClient,momentId,userId,token);
                }



            }
        });

        //设置评论数量
        tab_moment.getTabAt(0).setText("评论("+requestSingleMoment.getSingleUserMoment().getData().getCommentNum()+")");

        //设置赞数量
        tab_moment.getTabAt(1).setText("赞("+requestSingleMoment.getSingleUserMoment().getData().getLikeNum()+")");

        //设置是否赞了该动态
        if(requestSingleMoment.getSingleUserMoment().getData().isIsLike()==false){
            tv_moment_praise_text.setText("赞");
            iv_moment_praise.setImageResource(R.drawable.moment_praise);
        }else {
            tv_moment_praise_text.setText("已赞");
            iv_moment_praise.setImageResource(R.drawable.moment_praise_pressed);
        }


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

    @Override
    public void initNum(int num,int mode) {
        if(requestSingleMoment.getSingleUserMoment()==null||requestSingleMoment.getSingleUserMoment().getData()==null){
            return;
        }
        if(mode==RESET_COLUMN_NUM){
            requestSingleMoment.getSingleUserMoment().getData().setCommentNum(num);
            tab_moment.getTabAt(0).setText("评论("+requestSingleMoment.getSingleUserMoment().getData().getCommentNum()+")");
        }else if(mode==RESET_PRAISE_NUM){
            requestSingleMoment.getSingleUserMoment().getData().setLikeNum(num);
            tab_moment.getTabAt(1).setText("赞("+requestSingleMoment.getSingleUserMoment().getData().getLikeNum()+")");
        }

    }

    public interface OnLoadListViewReset {
        // TODO: Update argument type and name
        void resetLoadListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ProjectCommentActivity.REQUESR_CODE_SEND_COMMENT_ACTIVITY:
                if(resultCode==RESULT_OK){
                    if(requestSingleMoment.getSingleUserMoment()==null){
                        break;
                    }
                    //requestSingleMoment.getSingleUserMoment().getData().setCommentNum(requestSingleMoment.getSingleUserMoment().getData().getCommentNum()+1);
                    //tab_moment.getTabAt(0).setText("评论("+requestSingleMoment.getSingleUserMoment().getData().getCommentNum()+")");
                    ((MomentCommentFragment)fragmentList.get(0)).resetCommentList();
                }
                break;
            case LoginActivity.REQUEST_LOGIN_BY_PHONE:
                if(resultCode==RESULT_OK){
                    userId=LoginSituation.getUserId(this);
                    token=LoginSituation.getUserToken(this);
                    requestSingleMoment.requestSingleMoment(this,handler,httpClient,momentId,userId,userId);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
