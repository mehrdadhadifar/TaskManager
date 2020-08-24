package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskRepository;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String TAG = "TLF";
    private static final String ARG_STATES = "ARG_USERNAME";
    public static final int TASK_DETAIL_REQUEST_CODE = 0;
    public static final String TASK_DETAIL_FRAGMENT_TAG = "TASK_DETAIL_FRAGMENT_TAG";
    public static final String ARG_USER_UUID = "ARG_USER_UUID";
    private RecyclerView mRecyclerViewTasks;
    private LinearLayout mLinearLayoutEmpty;
    private TaskRepository mTaskRepository;
    private TaskAdapter mTaskAdapter;
    private List<State> mStateList;
    private UUID mUserId;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(List<State> stateList, UUID userId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATES, (Serializable) stateList);
        args.putSerializable(ARG_USER_UUID, userId);
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setArguments(args);
        return taskListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
    }

    private void initList() {
        mStateList = (List<State>) getArguments().getSerializable(ARG_STATES);
        mUserId = (UUID) getArguments().getSerializable(ARG_USER_UUID);
        mTaskRepository = TaskRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findAllViews(view);
        updateUI();
//        setClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskRepository = TaskRepository.getInstance();
        Log.d(TAG, "on resume");
        Log.d(TAG, "size list select:" + mTaskRepository.getListByStates(mStateList).size());
        updateUI();
    }


    private int getStateListPosition(UUID uuid) {
        for (int i = 0; i < mTaskRepository.getTasksByUserPerStates(mUserId, mStateList).size(); i++) {
            if (uuid.equals(mTaskRepository.getTasksByUserPerStates(mUserId, mStateList).get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    private void updateUI() {
        mRecyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Task> tasks = mTaskRepository.getTasksByUserPerStates(mUserId, mStateList);

        if (tasks.size() == 0) {
            mLinearLayoutEmpty.setVisibility(View.VISIBLE);
            mRecyclerViewTasks.setVisibility(View.GONE);
        } else {
            mLinearLayoutEmpty.setVisibility(View.GONE);
            mRecyclerViewTasks.setVisibility(View.VISIBLE);
        }
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getListByStates(mStateList).size()));
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getList().size()));
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mRecyclerViewTasks.setAdapter(mTaskAdapter);
        } else {
            Log.d(TAG, "Adapter Notify");
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewState;
        private LinearLayout mLinearLayoutMain;
        private TextView mTextViewComment;
        private TextView mTextViewDate;
        private Task mTask;
        private RoundedLetterView mRoundedLetterView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.recycle_view_tasks_text_view_title);
            mTextViewState = itemView.findViewById(R.id.recycle_view_tasks_text_view_state);
            mLinearLayoutMain = itemView.findViewById(R.id.recycle_view_tasks_main_linear_layout);
            mTextViewComment = itemView.findViewById(R.id.recycle_view_tasks_text_view_comment);
            mTextViewDate = itemView.findViewById(R.id.recycle_view_tasks_text_view_date);
            mRoundedLetterView = itemView.findViewById(R.id.rlv_name_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskDetailFragment taskDetailFragment = TaskDetailFragment.newInstance(mTask.getID(), mUserId);
                    setTargetFragment(taskDetailFragment, TASK_DETAIL_REQUEST_CODE);
                    taskDetailFragment.show(getFragmentManager(), TASK_DETAIL_FRAGMENT_TAG);
                }
            });
        }

        public void bindTask(Task task) {
            mTask = task;
            if (getItemViewType() == 1) {
                mTextViewTitle.setText(task.getTitle());
                mTextViewState.setText(task.getState().toString());
                mTextViewComment.setText(task.getComment());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(task.getDate());
                mTextViewDate.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
//                mTextViewDate.setText(calendar.get(Calendar.MONTH));
                if (getStateListPosition(task.getID()) % 2 == 1)
                    mLinearLayoutMain.setBackgroundColor(Color.GRAY);
                else
                    mLinearLayoutMain.setBackgroundColor((Color.WHITE));
                mRoundedLetterView.setTitleText(String.valueOf(task.getTitle().charAt(0)));
            }
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        List<Task> mTasks;

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public int getItemCount() {
            Log.d(TAG + " size adapteram", String.valueOf(mTasks.size()));
            return mTasks.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mTasks.size() == 0)
                return 0;
            else
                return 1;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view;
            if (viewType == 1)
                view = inflater.inflate(R.layout.recycler_view_list_row_task, parent, false);
            else
                view = inflater.inflate(R.layout.recycler_view_list_row_nothing, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }
    }

    private void findAllViews(View view) {
        mRecyclerViewTasks = view.findViewById(R.id.recycle_view_tasks);
        mLinearLayoutEmpty = view.findViewById(R.id.empty_linear_layout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null || resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == TASK_DETAIL_REQUEST_CODE) {
            updateUI();
        }
    }
}