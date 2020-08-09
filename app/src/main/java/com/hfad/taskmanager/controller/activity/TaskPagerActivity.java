package com.hfad.taskmanager.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.IRepository;
import com.hfad.taskmanager.repository.TaskRepository;

public class TaskPagerActivity extends AppCompatActivity {

    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;
    private IRepository<Task> mTaskRepository;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TaskPagerActivity.class);
        return intent;
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
            return null;
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