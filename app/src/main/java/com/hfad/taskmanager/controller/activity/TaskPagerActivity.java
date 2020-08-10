package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;

import java.util.ArrayList;
import java.util.List;

public class TaskPagerActivity extends AppCompatActivity {

    public static final String TAG = "TPA";
    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;

    public static Intent newIntent(Context context) {
        return new Intent(context, TaskPagerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        findAllViews();
        setUI();
    }




    private void setUI() {
        final FragmentStateAdapter adapter = new TaskViewPagerAdapter(this);
        mTaskViewPager.setAdapter(adapter);
        new TabLayoutMediator(mTaskTabLayout, mTaskViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0)
                            tab.setText("TODO");
                        else if (position == 1)
                            tab.setText("DOING");
                        else
                            tab.setText("DONE");
                    }
                }
        ).attach();
        mTaskViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG +" position:",String.valueOf(position));
//                adapter.notifyItemChanged(position);
                adapter.notifyItemRangeChanged(0,3);
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private class TaskViewPagerAdapter extends FragmentStateAdapter {
        public TaskViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            List<State> stateList = new ArrayList<>();
            switch (position) {
                case 1:
                    stateList.add(State.Doing);
                    break;
                case 2:
                    stateList.add(State.Done);
                    break;
                default:
                    stateList.add(State.Todo);
                    break;
            }
            return TaskListFragment.newInstance(stateList);
        }

        @Override
        public int getItemCount() {
            return State.values().length;
        }
    }


    private void findAllViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabLayout = findViewById(R.id.task_tab_layout);
    }
}