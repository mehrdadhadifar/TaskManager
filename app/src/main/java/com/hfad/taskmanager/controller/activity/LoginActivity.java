package com.hfad.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hfad.taskmanager.controller.fragment.BuildListFragment;
import com.hfad.taskmanager.controller.fragment.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}