package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.controller.fragment.TaskListFragment;

public class TaskListActivity extends SingleFragmentActivity {


    private static final String EXTRA_USERNAME = "com.hfad.taskmanager.controller.fragment_EXTRA_USERNAME";
    private static final String EXTRA_NUMBER_OF_TASKS = "com.hfad.taskmanager.controller.fragment_EXTRA_NUMBER_OF_TASKS";

    public static Intent newIntent(Context context, String username, int numberOfTasks) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_NUMBER_OF_TASKS, numberOfTasks);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        TaskListFragment taskListFragment = TaskListFragment.newInstance(
                getIntent().getStringExtra(EXTRA_USERNAME)
                , getIntent().getIntExtra(EXTRA_NUMBER_OF_TASKS, 0)
        );
        return taskListFragment;
    }

}