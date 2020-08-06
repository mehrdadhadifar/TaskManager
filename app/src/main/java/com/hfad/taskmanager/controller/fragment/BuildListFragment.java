package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.BuildListActivity;
import com.hfad.taskmanager.controller.activity.TaskListActivity;


public class BuildListFragment extends Fragment {

    private EditText mEditTextName;
    private EditText mEditTextNumber;
    private Button mButtonBuild;

    public BuildListFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        BuildListFragment buildListFragment = new BuildListFragment();
        buildListFragment.setArguments(args);
        return buildListFragment;
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
        findAllView(view);
        setClickListeners();
        return view;
    }

    private void setClickListeners() {
        mButtonBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextName.getText().length() == 0 || mEditTextNumber.getText().length() == 0)
                    Toast.makeText(getActivity(), "input is not valid", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = TaskListActivity.newIntent(getActivity(), mEditTextName.getText().toString(), Integer.valueOf(mEditTextNumber.getText().toString()));
                    startActivity(intent);
                }
            }
        });
    }

    private void findAllView(View view) {
        mEditTextName = view.findViewById(R.id.editText_username);
        mEditTextNumber = view.findViewById(R.id.editText_number_tasks);
        mButtonBuild = view.findViewById(R.id.button_build_tasks);
    }
}