package com.hfad.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hfad.taskmanager.controller.fragment.LoginFragment;
import com.hfad.taskmanager.controller.fragment.SignUpFragment;

public class SignUpActivity extends SingleFragmentActivity {


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return SignUpFragment.newInstance();
    }
}