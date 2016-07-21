package com.fansfunding.fan.project.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.fansfunding.fan.R;
import com.fansfunding.fan.project.activity.CreateProjectActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProjectRewordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateProjectRewordFragment extends Fragment{
    SimpleAdapter simpleAdapter;
    private static CreateProjectRewordFragment createProjectRewordFragment;

    public void refreshAdapter(){
        simpleAdapter.notifyDataSetChanged();
    }

    public CreateProjectRewordFragment() {
        // Required empty public constructor
    }

    public static CreateProjectRewordFragment getInstance(){
        return createProjectRewordFragment;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProjectRewordFragment newInstance() {

        CreateProjectRewordFragment fragment = new CreateProjectRewordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        createProjectRewordFragment = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    SwipeMenuListView listView;
    FloatingActionButton btn_add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_create_project_reword, container, false);


        listView=(SwipeMenuListView)rootView.findViewById(R.id.lv_project_create_reward);
        btn_add=(FloatingActionButton)rootView.findViewById(R.id.btn_project_create_add_reward);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProjectActivity.getInstance().setPageState(2);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {


                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        CreateProjectRewordFragment.this.getContext());
                // set item width
                deleteItem.setWidth(getResources().getDimensionPixelOffset(R.dimen.item_content_padding_left_with_parent));
                // set a icon
                deleteItem.setIcon(android.R.drawable.ic_lock_lock);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        simpleAdapter=new SimpleAdapter(this.getContext(),CreateProjectActivity.getInstance().getList(),R.layout.item_project_create_new_reward,
                new String[]{"msg_name","msg_content"},
                new int[]{R.id.tv_msg_name,R.id.tv_msg_content});
        listView.setAdapter(simpleAdapter);

        // set creator
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.e("TEST",position+"");
                switch (index) {
                    case 0:
                        Log.e("TEST",position+"");
                        CreateProjectActivity.getInstance().removeItem(position);
                        simpleAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        return rootView;
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

}
