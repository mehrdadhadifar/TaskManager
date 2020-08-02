package com.hfad.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.TaskListFragment;

public class TaskListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new TaskListFragment();
    }
}