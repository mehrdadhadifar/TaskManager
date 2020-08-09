package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.IRepository;
import com.hfad.taskmanager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskPagerActivity extends AppCompatActivity {

    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;
    private IRepository<Task> mTaskRepository;

    public static Intent newIntent(Context context) {
        return new Intent(context, TaskPagerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        mTaskRepository = TaskRepository.getInstance();
        findAllViews();
        FragmentStateAdapter adapter = new TaskViewPagerAdapter(this);
        mTaskViewPager.setAdapter(adapter);
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