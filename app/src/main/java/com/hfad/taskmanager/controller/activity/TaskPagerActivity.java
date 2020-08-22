package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.fragment.NewTaskFragment;
import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskPagerActivity extends AppCompatActivity {

    public static final String TAG = "TPA";
    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;
    private FloatingActionButton mFloatingActionButtonNewTask;
    private TaskRepository mTaskRepository;

    public static Intent newIntent(Context context) {
        return new Intent(context, TaskPagerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        mTaskRepository = TaskRepository.getInstance();
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
        mFloatingActionButtonNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button activity");
                Log.d(TAG, "repository size:" + mTaskRepository.getList().size());
                NewTaskFragment newTaskFragment = NewTaskFragment.newInstance();
                newTaskFragment.show(getSupportFragmentManager(), "NEW_TASK_FRAGMENT");
/*                adapter.notifyItemChanged(mTaskViewPager.getCurrentItem());
                adapter.notifyDataSetChanged();
                adapter.notifyItemRangeChanged(0,3);*/
                int position = mTaskViewPager.getCurrentItem();
                setUI();
                mTaskViewPager.setCurrentItem(position);
            }
        });

/*        mTaskViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG +" position:",String.valueOf(position));
//                adapter.notifyItemChanged(mTaskViewPager.getCurrentItem());
                adapter.notifyItemRangeChanged(0,3);
//                adapter.notifyDataSetChanged();
//                adapter.createFragment(mTaskViewPager.getCurrentItem());
//                adapter.notifyItemChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });*/
    }

    private class TaskViewPagerAdapter extends FragmentStateAdapter {
        public TaskViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            List<State> stateList = getStateList(position);
            return TaskListFragment.newInstance(stateList);
        }

        @Override
        public int getItemCount() {
            return State.values().length;
        }
    }

    private List<State> getStateList(int position) {
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
        return stateList;
    }


    private void findAllViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabLayout = findViewById(R.id.task_tab_layout);
        mFloatingActionButtonNewTask = findViewById(R.id.floating_Action_Button_add_pager);
    }
}