package com.fansfunding.fan;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_setting_account);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();

        toolbar.setTitleTextColor(Color.WHITE);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("账号和密码");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        RelativeLayout rl_setting_account_reset_password=(RelativeLayout)findViewById(R.id.rl_setting_account_reset_password);
        rl_setting_account_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_reset_password));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                setResult(0);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

}
