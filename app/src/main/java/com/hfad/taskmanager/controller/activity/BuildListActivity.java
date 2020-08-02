package com.hfad.taskmanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.fragment.BuildListFragment;

public class BuildListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_list);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,new BuildListFragment())
                .commit();
    }
}