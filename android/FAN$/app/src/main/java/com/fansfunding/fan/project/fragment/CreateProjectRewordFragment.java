package com.fansfunding.fan.project.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateProjectRewordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateProjectRewordFragment extends Fragment{
    //Views
    private static CreateProjectRewordFragment createProjectRewordFragment;
    private SwipeMenuListView lv_project_create_reward;
    private FloatingActionButton btn_add;

    //Adapters
    private SimpleAdapter simpleAdapter;

    //ViewOfSwipeMenu
    private SwipeMenuCreator creator = new SwipeMenuCreator() {
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


    public CreateProjectRewordFragment() {}

    //Adapter Controller
    public void refreshAdapter(){
        simpleAdapter.notifyDataSetChanged();
    }

    public static CreateProjectRewordFragment getInstance(){
        return createProjectRewordFragment;
    }

    public static CreateProjectRewordFragment newInstance() {
        CreateProjectRewordFragment fragment = new CreateProjectRewordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        createProjectRewordFragment = fragment;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_create_project_reword, container, false);
        initView(rootView);
        return rootView;
    }


    private void initView(View rootView) {
        lv_project_create_reward=(SwipeMenuListView)rootView.findViewById(R.id.lv_project_create_reward);
        btn_add=(FloatingActionButton)rootView.findViewById(R.id.btn_project_create_add_reward);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProjectActivity.getInstance().setPageState(2);
            }
        });
        simpleAdapter=new SimpleAdapter(this.getContext(),CreateProjectActivity.getInstance().getRewardList(),R.layout.item_project_create_new_reward,
                new String[]{"msg_name","msg_content"},
                new int[]{R.id.tv_msg_name,R.id.tv_msg_content});
        initListView();
    }


    private void initListView() {
        lv_project_create_reward.setAdapter(simpleAdapter);
        // set creator
        lv_project_create_reward.setMenuCreator(creator);
        lv_project_create_reward.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        lv_project_create_reward.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        CreateProjectActivity.getInstance().removeItem(position);
                        break;
                }
                return false;
            }
        });
    }
}
