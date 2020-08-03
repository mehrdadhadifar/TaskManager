package com.hfad.taskmanager.controller.activity;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.controller.fragment.BuildListFragment;

public class BuildListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new BuildListFragment();
    }


}