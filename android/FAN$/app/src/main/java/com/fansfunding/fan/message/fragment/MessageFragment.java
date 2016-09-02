package com.fansfunding.fan.message.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fansfunding.fan.R;
import com.fansfunding.fan.message.adapter.MessageFragmentAdapter;

/**
 * Created by RJzz on 2016/8/25.
 */
public class MessageFragment extends Fragment {

    private static final String ARGUMENT = "arguments";

    private String mArgument;
    private View viewContent;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private String[] mTitle = {"私信", "评论", "通知"};

    public MessageFragment() {

    }

    public static MessageFragment newInstance(String argument) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT, argument);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            mArgument = bundle.getString(ARGUMENT);
        }

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContent = inflater.inflate(R.layout.fragment_message_main, container, false);

        initContentView(viewContent);
        initData();
        if(mArgument == "notificaition") {
            viewPager.setCurrentItem(2);
        }
        Intent intent = getActivity().getIntent();
        int i = intent.getIntExtra("page", 0);
        viewPager.setCurrentItem(i);

        return viewContent;
    }

    @Override
    public void onResume() {

        super.onResume();

    }

    private void initData() {
        MessageFragmentAdapter adapter = new MessageFragmentAdapter(getChildFragmentManager());
        this.viewPager.setAdapter(adapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }

    private void initContentView(View viewContent) {
        this.tabLayout = (TabLayout) viewContent.findViewById(R.id.tl_message_main);
        this.viewPager = (ViewPager) viewContent.findViewById(R.id.vp_message_main);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


//    private PagerAdapter mAdapter = new PagerAdapter() {
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mTitle[position];
//        }
//
//        @Override
//        public int getCount() {
//            return mTitle.length;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager) container).removeView((View) object);
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1003:
                Intent intent = getActivity().getIntent();
                int i = intent.getIntExtra("page", 0);
                viewPager.setCurrentItem(i);
                break;
            default:
                break;
        }
    }
}
