package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.controller.fragment.UserListFragment;

import java.util.UUID;

public class UserListActivity extends SingleFragmentActivity {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return UserListFragment.newInstance( getIntent().getLongExtra(EXTRA_USER_ID,0));
    }
}