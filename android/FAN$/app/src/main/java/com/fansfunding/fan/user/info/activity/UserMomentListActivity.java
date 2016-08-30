package com.fansfunding.fan.user.info.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fansfunding.fan.R;
import com.fansfunding.fan.social.fragment.SocialFragment;

public class UserMomentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_moment_list);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_moment, SocialFragment.newInstance())
                .commit();
    }
}
