package com.fansfunding.fan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* 用来显示聊天界面
* */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("冰冻vita");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listView=(ListView)findViewById(R.id.lv_chat_list);

        //构建simpleadapter
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<1;i++){
            Map<String,Object> tempMap=new HashMap<String, Object>();
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_chat,
                new String[]{},
                new int[]{});



        listView.setAdapter(simpleAdapter);
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
