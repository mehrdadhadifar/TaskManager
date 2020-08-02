package com.hfad.taskmanager.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.taskmanager.R;


public class TaskListFragment extends Fragment {
    private RecyclerView mRecyclerViewTasks;

    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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