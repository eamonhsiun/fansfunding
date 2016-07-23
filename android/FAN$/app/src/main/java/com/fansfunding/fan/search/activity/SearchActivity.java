package com.fansfunding.fan.search.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fansfunding.fan.R;
import com.fansfunding.fan.search.adapter.SearchPaperAdapter;

/**
 * 搜索界面
 */
public class SearchActivity extends AppCompatActivity {

    private SearchPaperAdapter paperAdapter;
    private TextInputEditText tiet_search_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        //设置返回键
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow);
        actionBar.setTitle(null);

        tiet_search_input=(TextInputEditText)findViewById(R.id.tiet_search_input);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_search);
        ViewPager viewPager=(ViewPager)findViewById(R.id.vp_search);
        paperAdapter=new SearchPaperAdapter(getSupportFragmentManager());
        //设置搜索关键字
        paperAdapter.setKeyword(tiet_search_input.getText().toString());
        viewPager.setAdapter(paperAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        tiet_search_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
                    if(tiet_search_input.getText().toString()==null||tiet_search_input.getText().toString().equals("")){
                        Toast.makeText(SearchActivity.this,"请输入要搜索的内容",Toast.LENGTH_LONG).show();
                        return true;
                    }
                    paperAdapter.setKeyword(tiet_search_input.getText().toString());
                    paperAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
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
