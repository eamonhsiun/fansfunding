package com.fansfunding.fan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


/**
 * 用来创建他人主页的界面
 *
 *  */
public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_homepage);
        setSupportActionBar(toolbar);

        //设置返回键
        ActionBar actionbar=this.getSupportActionBar();
        actionbar.setTitle("冰冻vita");
        actionbar.setDisplayHomeAsUpEnabled(true);

        //int [] view_list={R.id.rl_homepage_PJ_publish,R.id.rl_homepage_PJ_follow,R.id.rl_homepage_PJ_support};
        View view_publish=findViewById(R.id.rl_homepage_PJ_publish);
        view_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project));
                intent.putExtra("number",0);
                startActivity(intent);
            }
        });


        View view_follow=findViewById(R.id.rl_homepage_PJ_follow);
        view_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project));
                intent.putExtra("number",1);
                startActivity(intent);
            }
        });

        View view_support=findViewById(R.id.rl_homepage_PJ_support);
        view_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_project));
                intent.putExtra("number",2);
                startActivity(intent);
            }
        });


        View view_send=findViewById(R.id.iv_homepage_chat);
        view_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(getString(R.string.activity_chat));
                startActivity(intent);
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
}
