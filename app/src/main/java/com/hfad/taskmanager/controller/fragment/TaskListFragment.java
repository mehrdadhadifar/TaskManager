package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.IRepository;
import com.hfad.taskmanager.repository.TaskRepository;


public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerViewTasks;
    private IRepository mRepository;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra(BuildListFragment.EXTRA_USERNAME);
        int numberOfTasks = intent.getIntExtra(BuildListFragment.EXTRA_NUMBER_OF_TASKS, 5);
        mRepository = TaskRepository.getInstance();
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


        return view;
    }

    private void findAllViews(View view) {
        mRecyclerViewTasks = view.findViewById(R.id.recycle_view_tasks);
    }
}