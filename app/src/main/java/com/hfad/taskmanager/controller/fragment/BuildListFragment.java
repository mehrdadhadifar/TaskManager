package com.hfad.taskmanager.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.Task;


public class BuildListFragment extends Fragment {
    private TextView mTextViewTest;

    public BuildListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_build_list, container, false);
        mTextViewTest = view.findViewById(R.id.textView_test);
        mTextViewTest.setText(new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString() + "\n" + new Task("MY TEST TASK").toString());
        return view;
    }
}