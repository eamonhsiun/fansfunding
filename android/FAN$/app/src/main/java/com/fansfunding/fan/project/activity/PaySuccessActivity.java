package com.fansfunding.fan.project.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fansfunding.fan.R;

import java.lang.reflect.InvocationTargetException;

public class PaySuccessActivity extends AppCompatActivity {


    private String orderNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);

        Intent intent=getIntent();
        orderNo=intent.getStringExtra("orderNo");

        TextView tv_pay_success_check_info=(TextView)findViewById(R.id.tv_pay_success_check_info);
        tv_pay_success_check_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开订单详情界面
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_user_order_detail));
                intent.putExtra("orderNo",orderNo);
                startActivity(intent);

                //关闭支付成功界面
                if(PaySuccessActivity.this.isFinishing()==false){
                    PaySuccessActivity.this.finish();
                }
            }
        });

    }
}
