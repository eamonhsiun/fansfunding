package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.adapter.ProjectSupportAdapter;
import com.fansfunding.fan.project.utils.PayResult;
import com.fansfunding.fan.request.RequestAddressDefault;
import com.fansfunding.fan.request.RequestAlipay;
import com.fansfunding.fan.request.RequestProjectDetailReward;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.Address;
import com.fansfunding.internal.ProjectDetailReward;
import com.fansfunding.internal.ProjectInfo;
import com.fansfunding.internal.project.FANAlipay;
import com.fansfunding.internal.user.AddressDefault;
import com.nostra13.universalimageloader.utils.L;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ProjectSupportActivity extends AppCompatActivity {

    //打开地址的请求码
    private final static int REQUEST_CODE_START_ADDRESS_ACTIVITY=100;

    //用来请求项目回报
    private RequestProjectDetailReward requestProjectDetailReward;

    //用来请求默认地址
    private RequestAddressDefault requestAddressDefault;

    //用来请求生成支付宝订单
    private RequestAlipay requestAlipay;

    //是否正在生成订单
    private boolean isCreatingOrder=false;

    //分类Id
    private int catogoryId;

    //项目id
    private int projectId;

    //通过查找分类下所有项目所获取的数据
    private ProjectInfo detail;

    //获取到的默认地址
    private AddressDefault address=null;

    //获取到的回报信息
    private ProjectDetailReward reward=null;

    //获取到的订单信息
    private FANAlipay fanAlipay=null;

    //回报列表适配器
    private ProjectSupportAdapter adapter;

    private ListView lv_PJ_support_reward;

    //尾部的View
    private View footer;

    //地址展示栏
    private TextView tv_support_address;

    //收货人展示栏
    private TextView tv_project_support_people_name;

    //收货人电话展示栏
    private TextView tv_project_support_people_phone;


    //合计金额展示栏
    private TextView tv_PJ_support_need_money;

    //支付宝选择框
    private CheckBox checkBox_alipay;

    //httpclient
    //private OkHttpClient httpClient;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FANRequestCode.GET_USER_DEFAULT_ADDRESS_SUCCESS:
                    address=requestAddressDefault.getAddress();
                    InitAddressDefault();
                    break;
                case FANRequestCode.GET_USER_DEFAULT_ADDRESS_FAILURE:
                    if(ProjectSupportActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectSupportActivity.this,"请求默认地址失败",Toast.LENGTH_LONG).show();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_REWARD_SUCCESS:
                    reward=requestProjectDetailReward.getReward();
                    InitProjectDetailReward();
                    break;
                case FANRequestCode.GET_PROJECT_DETAIL_REWARD_FAILURE:
                    if(ProjectSupportActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectSupportActivity.this,"请求项目回报失败",Toast.LENGTH_LONG).show();
                    break;

                case FANRequestCode.GET_MOBILE_ALIPAY_SUCCESS:
                    fanAlipay=requestAlipay.getAlipay();
                    nextAlipay();
                    break;
                case FANRequestCode.GET_MOBILE_ALIPAY_FAILURE:
                    //已完成订单，虽然是失败的
                    isCreatingOrder=false;
                    if(ProjectSupportActivity.this.isFinishing()==true){
                        break;
                    }
                    Toast.makeText(ProjectSupportActivity.this,"订单生产失败，请重试",Toast.LENGTH_LONG).show();
                    break;
                case FANRequestCode.SDK_PAY_FLAG:
                    //完成订单，此时是成功的，就是没有bug的
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ProjectSupportActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ProjectSupportActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ProjectSupportActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    isCreatingOrder=false;
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_support);

        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_PJ_support);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("支持");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        //初始化
        adapter=new ProjectSupportAdapter(this);
        requestProjectDetailReward =new RequestProjectDetailReward();
        requestAddressDefault=new RequestAddressDefault();
        requestAlipay=new RequestAlipay();
        //httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();


        Intent intent=getIntent();
        detail= (ProjectInfo) intent.getSerializableExtra("detail");

        catogoryId=detail.getCategoryId();
        projectId=detail.getId();


        //回报列表
        lv_PJ_support_reward=(ListView)findViewById(R.id.lv_PJ_support_reward);

        //合计金额展示栏
        //tv_PJ_support_need_money=(TextView)findViewById(R.id.tv_PJ_support_need_money);

        //尾部控件
        footer=View.inflate(this,R.layout.activity_project_support_footer,null);

        //支付宝选择框
        checkBox_alipay=(CheckBox)footer.findViewById(R.id.checkbox_project_support_alipay) ;

        //地址展示栏
        tv_support_address=(TextView) footer.findViewById(R.id.tv_support_address);

        //收货人展示栏
        tv_project_support_people_name=(TextView)footer.findViewById(R.id.tv_project_support_people_name);
        //收获人电话展示栏
        tv_project_support_people_phone=(TextView)footer.findViewById(R.id.tv_project_support_people_phone);

        //设置修改地址按钮
        LinearLayout ll_project_support_address=(LinearLayout)footer.findViewById(R.id.ll_project_support_address);
        ll_project_support_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_address));
                intent.putExtra("needSelect",true);
                startActivityForResult(intent,REQUEST_CODE_START_ADDRESS_ACTIVITY);
            }
        });


        lv_PJ_support_reward.addFooterView(footer);

        //设置adapter
        lv_PJ_support_reward.setAdapter(adapter);


        //下一步
        TextView tv_PJ_support_next=(TextView)findViewById(R.id.tv_PJ_support_next);
        tv_PJ_support_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectDetailNext();
            }
        });

        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

        //获取默认地址
        requestAddressDefault.getDefaultAddress(this,handler,httpClient);

        //获取默认回报
        requestProjectDetailReward.getProjectDetailReward(this,handler,httpClient,catogoryId,projectId);
    }


    //初始化项目回报
    private void InitProjectDetailReward(){
        if(reward==null||reward.getData().getList().size()==0){
            return;
        }
        for(int i=0;i<reward.getData().getList().size();i++){
            adapter.addItem(reward.getData().getList().get(i));
        }
        adapter.notifyDataSetChanged();
    }

    //初始化默认地址信息
    private void InitAddressDefault(){
        if(address==null||address.getData()==null){
            return;
        }
        if(address.getData().getAddress()==null||address.getData().getProvince()==null||address.getData().getCity()==null||address.getData().getDistrict()==null){
            Log.i("TAG","FAILUREINADDRESS");
            return;
        }
        tv_support_address.setText("收货地址:"+address.getData().getProvince()+address.getData().getCity()+address.getData().getDistrict()+address.getData().getAddress());
        tv_project_support_people_phone.setText(address.getData().getPhone());
        tv_project_support_people_name.setText(address.getData().getName());
    }

    //修改合计金额
    private void ChangeTotalAmount(){
        if(adapter!=null&&adapter.getSelectedItem()!=null&&adapter.getSelectedItem().getLimitation()!=null){
            tv_PJ_support_need_money.setText(new java.text.DecimalFormat("0.00").format(adapter.getSelectedItem().getLimitation()));
        }else{
            tv_PJ_support_need_money.setText(String.valueOf(0));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_START_ADDRESS_ACTIVITY:
                if(resultCode==RESULT_OK){
                    if((Address.DataDetial)data.getSerializableExtra("address")==null){
                        Log.i("TAG","RETURN NULL");
                    }
                    address.setData((Address.DataDetial)data.getSerializableExtra("address"));
                    Log.i("TAG","NOT NULL");
                    InitAddressDefault();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //下一步按钮的响应函数
    private void projectDetailNext(){
        ProjectDetailReward.ProjectReward detail=(ProjectDetailReward.ProjectReward)adapter.getSelectedItem();
        //未登录
        SharedPreferences share=getSharedPreferences(getString(R.string.sharepreference_login_by_phone),MODE_PRIVATE);
        if(share.getBoolean("isLogin",false)==false){
            Toast.makeText(ProjectSupportActivity.this,"请登录",Toast.LENGTH_LONG).show();
            return;
        }
        //未选择回报
        if(detail==null){
            Toast.makeText(ProjectSupportActivity.this,"请选择回报",Toast.LENGTH_SHORT).show();
            return;
        }
        //未选择支付方式
        if(checkBox_alipay.isChecked()==false){
            Toast.makeText(ProjectSupportActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
            return;
        }
        if(address==null||address.getData()==null||address.getData().getAddress()==null||address.getData().getProvince()==null||address.getData().getCity()==null||address.getData().getDistrict()==null){
            Toast.makeText(ProjectSupportActivity.this,"请选择地址",Toast.LENGTH_LONG).show();
            return;
        }
        if(isCreatingOrder==true){
            Toast.makeText(ProjectSupportActivity.this,"正在生成订单信息，请稍等",Toast.LENGTH_LONG).show();
            return;
        }

        //将状态改为正在生成订单信息
        isCreatingOrder=true;
        OkHttpClient httpClient=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        requestAlipay.getAlipayOrder(ProjectSupportActivity.this,handler,httpClient,detail.getId(),share.getInt("id",0),address.getData().getAddressId());
    }

    //启动支付宝
    private void nextAlipay(){
        if(fanAlipay==null||fanAlipay.getData()==null){
            return;
        }
        final String orderInfo=fanAlipay.getData().getSignedOrder().replace("\\","");

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ProjectSupportActivity.this);
                String result = alipay.pay(orderInfo,true);
                Message msg = new Message();
                msg.what = FANRequestCode.SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
