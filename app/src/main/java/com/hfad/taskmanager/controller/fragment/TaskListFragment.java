package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.IRepository;
import com.hfad.taskmanager.repository.TaskRepository;

import java.util.List;


public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerViewTasks;
    private IRepository<Task> mRepository;
    private TaskAdapter mTaskAdapter;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildTasksRepository();
    }
    private void buildTasksRepository() {
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra(BuildListFragment.EXTRA_USERNAME);
        int numberOfTasks = intent.getIntExtra(BuildListFragment.EXTRA_NUMBER_OF_TASKS, 5);
        mRepository = TaskRepository.getInstance();
        if (mRepository == null)
            Log.d("TLF", "mRepository is null");
        else
            Log.d("TLF", "mRepository is not null");

        for (int i = 0; i < numberOfTasks; i++) {
            mRepository.insert(new Task(username));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findAllViews(view);
        updateList();
        return view;
    }

    private void updateList() {
        mRecyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Task> tasks = mRepository.getList();
        if (mTaskAdapter == null) {
            mTaskAdapter=new TaskAdapter(tasks);
            mRecyclerViewTasks.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewState;
        private LinearLayout mLinearLayoutMain;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.recycle_view_tasks_text_view_title);
            mTextViewState = itemView.findViewById(R.id.recycle_view_tasks_text_view_state);
            mLinearLayoutMain = itemView.findViewById(R.id.recycle_view_tasks_main_linear_layout);
        }

        public void bindTask(Task task) {
            mTextViewTitle.setText(task.getTitle());
            mTextViewState.setText(task.getState().toString());
            if (getAdapterPosition() % 2 == 1)
                mLinearLayoutMain.setBackgroundColor(Color.GRAY);
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
            return mTasks.size();
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.recycler_view_list_row_task, parent, false);
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
    }


}