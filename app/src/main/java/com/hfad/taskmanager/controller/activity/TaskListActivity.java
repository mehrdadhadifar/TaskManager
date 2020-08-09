package com.hfad.taskmanager.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.controller.fragment.TaskListFragment;
import com.hfad.taskmanager.model.State;

import java.io.Serializable;
import java.util.List;

public class TaskListActivity extends SingleFragmentActivity {


    private static final String EXTRA_USERNAME = "com.hfad.taskmanager.controller.fragment_EXTRA_USERNAME";
    private static final String EXTRA_STATE_LIST = "com.hfad.taskmanager.controller.fragment_EXTRA_NUMBER_OF_TASKS";

    /**
     * This activity needs a list of States to show tasks list;
     *
     * @param context   the source Context;
     * @param stateList list of States which have to e shown;
     * @return
     */
    public static Intent newIntent(Context context, List<State> stateList) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_STATE_LIST, (Serializable) stateList);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        TaskListFragment taskListFragment = TaskListFragment.newInstance(
                (List<State>) getIntent().getSerializableExtra(EXTRA_STATE_LIST)
        );
        return taskListFragment;
    }

}