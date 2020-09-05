package com.hfad.taskmanager.controller.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.hfad.taskmanager.controller.fragment.TaskDetailFragment;
import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskPagerActivity extends AppCompatActivity implements TaskDetailFragment.Callbacks {

    public static final String TAG = "TPA";
    public static final String NEW_TASK_FRAGMENT = "NEW_TASK_FRAGMENT";
    public static final String EXTRA_USER_UUID = "EXTRA_USER_UUID";
    private ViewPager2 mTaskViewPager;
    private TabLayout mTaskTabLayout;
    private FloatingActionButton mFloatingActionButtonNewTask;
    private TaskDBRepository mTaskRepository;
    private long mUserId;
    private UserDBRepository mUserDBRepository;

    FragmentStateAdapter adapter;


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
        mUserId = getIntent().getLongExtra(EXTRA_USER_UUID,0);
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
                TaskDetailFragment newTaskDetailFragment = TaskDetailFragment.newInstance(null, mUserId);
                newTaskDetailFragment.show(getSupportFragmentManager(), NEW_TASK_FRAGMENT);
            }
        });

/*        mTaskViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG +" position:",String.valueOf(position));
//                adapter.notifyItemChanged(mTaskViewPager.getCurrentItem());
                adapter.notifyItemRangeChanged(0,3);
//                adapter.notifyDataSetChanged();
//                adapter.createFragment(mTaskViewPager.getCurrentItem());
//                adapter.notifyItemChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });*/
    }


    public void setUI() {
        getSupportActionBar().setSubtitle(mUserDBRepository.get(mUserId).getUsername() + " Tasks");
        if (mUserDBRepository.get(mUserId).getRole() == 1)
            mFloatingActionButtonNewTask.setEnabled(false);
//        if (adapterr == null) {
        adapter = new TaskViewPagerAdapter(this);
        mTaskViewPager.setAdapter(adapter);
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
/*        } else {
//            adapter.notifyItemChanged(mTaskViewPager.getCurrentItem());
            adapter.createFragment(mTaskViewPager.getCurrentItem());
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(0, 3);
            synchronized (adapter) {
                adapter.notifyAll();
            }
        }*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_all:
                new AlertDialog.Builder(TaskPagerActivity.this)
                        .setTitle("Are sure to delete all tasks?")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Task> list = mTaskRepository.getUserTasks(mUserId);
                                for (int j = 0; j < list.size(); j++) {
                                    mTaskRepository.delete(list.get(j));
                                }
                                setUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create().show();
                return true;
            case R.id.menu_item_sign_out:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void findAllViews() {
        mTaskViewPager = findViewById(R.id.task_view_pager);
        mTaskTabLayout = findViewById(R.id.task_tab_layout);
        mFloatingActionButtonNewTask = findViewById(R.id.floating_Action_Button_add_pager);
    }
}