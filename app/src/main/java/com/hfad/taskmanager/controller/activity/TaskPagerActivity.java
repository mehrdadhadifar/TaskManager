package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.fragment.DialogTaskDetailFragment;
import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskPagerActivity extends AppCompatActivity implements DialogTaskDetailFragment.Callbacks {

    public static final String TAG = "TPA";
    public static final String NEW_TASK_FRAGMENT = "NEW_TASK_FRAGMENT";
    public static final String EXTRA_USER_UUID = "EXTRA_USER_UUID";
    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;
    private FloatingActionButton mFloatingActionButtonNewTask;
    private TaskDBRepository mTaskRepository;
    private long mUserId;
    private UserDBRepository mUserDBRepository;

    FragmentStateAdapter mAdapter;


    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, TaskPagerActivity.class);
        intent.putExtra(EXTRA_USER_UUID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pager);
        mTaskRepository = TaskDBRepository.getInstance(this);
        mUserDBRepository = UserDBRepository.getInstance(this);
        mUserId = getIntent().getLongExtra(EXTRA_USER_UUID, 0);
        findAllViews();
        setUI();
        setonClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            if (getSupportFragmentManager().getFragments().get(i) instanceof TaskListFragment) {
                ((TaskListFragment) (getSupportFragmentManager().getFragments().get(i))).updateUI();
            }
        }
    }

    private void setonClickListeners() {
        mFloatingActionButtonNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button activity");
                Log.d(TAG, "repository size:" + mTaskRepository.getList().size());
                DialogTaskDetailFragment newDialogTaskDetailFragment = DialogTaskDetailFragment.newInstance(null, mUserId);
                newDialogTaskDetailFragment.show(getSupportFragmentManager(), NEW_TASK_FRAGMENT);
            }
        });
    }


    public void setUI() {
        getSupportActionBar().setSubtitle(mUserDBRepository.get(mUserId).getUsername() + " Tasks");
        if (mUserDBRepository.get(mUserId).getRole() == 1)
            mFloatingActionButtonNewTask.setEnabled(false);
//        if (adapterr == null) {
        mAdapter = new TaskViewPagerAdapter(this);
        mTaskViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTaskTabLayout, mTaskViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0)
                            tab.setText("TODO");
                        else if (position == 1)
                            tab.setText("DOING");
                        else
                            tab.setText("DONE");
                    }
                }
        ).attach();
    }

    @Override
    public void onTaskUpdate() {
        updateUI();
    }

    private class TaskViewPagerAdapter extends FragmentStateAdapter {
        public TaskViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            List<State> stateList = getStateList(position);
            return TaskListFragment.newInstance(stateList, mUserId);
        }

        @Override
        public int getItemCount() {
            return State.values().length;
        }
    }

    private List<State> getStateList(int position) {
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
        return stateList;
    }





    private void findAllViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabLayout = findViewById(R.id.task_tab_layout);
        mFloatingActionButtonNewTask = findViewById(R.id.floating_Action_Button_add_pager);
    }
}