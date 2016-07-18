package com.fansfunding.fan;

import android.content.Intent;
import android.graphics.Color;
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

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_address);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.RED);
        Intent intent=getIntent();

        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setTitle("管理收获地址");
        actionBar.setDisplayHomeAsUpEnabled(true);


        ListView lv_address=(ListView)findViewById(R.id.lv_address);
        List<Map<String,Object>> listItems=new ArrayList<Map<String, Object>>();
        for(int i=0;i<2;i++) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            listItems.add(tempMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_address,
                new String[]{},
                new int[]{});
        lv_address.setAdapter(simpleAdapter);
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
