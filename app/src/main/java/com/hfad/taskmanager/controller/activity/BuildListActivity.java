package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.controller.fragment.BuildListFragment;

public class BuildListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, BuildListActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return BuildListFragment.newInstance();
    }
}