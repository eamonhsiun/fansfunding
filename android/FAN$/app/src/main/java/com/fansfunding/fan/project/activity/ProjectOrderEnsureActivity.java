package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.utils.PayResult;
import com.fansfunding.fan.request.RequestAlipay;
import com.fansfunding.fan.utils.ErrorHandler;
import com.fansfunding.fan.utils.FANRequestCode;
import com.fansfunding.internal.project.FANAlipay;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ProjectOrderEnsureActivity extends AppCompatActivity {

    //支付成功请求码
    private final static int REQUEST_CODE_PAY_SUCCESS=9000;

    //支付结果确认中请求码
    private final static int REQUEST_CODE_PAY_ENSURING=8000;

    //支付失败请求码
    private final static int REQUEST_CODE_PAY_FAILURE=7000;


    //订单签名
   private String orderInfo;

    //订单编号
    private String orderNo;

    //支付方式选择框
    private CheckBox checkBox_alipay;

    //支付金额
    private String rewardPrice;

    private ErrorHandler handler=new ErrorHandler(this){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
                        //Toast.makeText(ProjectOrderEnsureActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                        //打开支付成功界面
                        Intent intent=new Intent();
                        intent.setAction(getString(R.string.activity_pay_success));
                        intent.putExtra("orderNo",orderNo);
                        startActivity(intent);

                        //付款成功，结束此界面
                        setResult(RESULT_OK);
                        if(ProjectOrderEnsureActivity.this.isFinishing()==false){
                            ProjectOrderEnsureActivity.this.finish();
                        }
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ProjectOrderEnsureActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ProjectOrderEnsureActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
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
        setContentView(R.layout.activity_project_order_ensure);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_project_order);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("支付订单");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        orderInfo=intent.getStringExtra("orderInfo");
        orderNo=intent.getStringExtra("orderNo");
        rewardPrice=intent.getStringExtra("rewardPrice");
        //订单编号
        TextView tv_project_order_no=(TextView)findViewById(R.id.tv_project_order_no);
        if(orderNo!=null){
            tv_project_order_no.setText(orderNo);
        }

        //项目名称
        TextView tv_project_order_name=(TextView)findViewById(R.id.tv_project_order_name);
        if(intent.getStringExtra("projectName")!=null){
            tv_project_order_name.setText(intent.getStringExtra("projectName"));
        }

        //项目图片
        ImageView iv_project_order_image=(ImageView)findViewById(R.id.iv_project_order_image);
        if(intent.getStringExtra("projectPhoto")!=null&&intent.getStringExtra("projectPhoto").equals("")==false){
            Picasso.with(this).load(intent.getStringExtra("projectPhoto")).resize(70,70).centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_project_order_image);
        }

        //回报所需金额
        TextView tv_project_order_price=(TextView)findViewById(R.id.tv_project_order_price);
        if(rewardPrice!=null){
            tv_project_order_price.setText(rewardPrice);
        }

        //支付方式选择框
        checkBox_alipay=(CheckBox)findViewById(R.id.checkbox_project_support_alipay) ;

        Button btn_project_order_ensure_pay=(Button)findViewById(R.id.btn_project_order_ensure_pay);
        btn_project_order_ensure_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_alipay.isChecked()==false){
                    Toast.makeText(ProjectOrderEnsureActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                    return;
                }
                nextAlipay();
            }
        });


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


    //启动支付宝
    private void nextAlipay(){
        if(orderInfo==null||orderInfo.equals("")){
            return;
        }
        final String temp_orderInfo=orderInfo.replace("\\","");

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ProjectOrderEnsureActivity.this);
                String result = alipay.pay(temp_orderInfo,true);
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
