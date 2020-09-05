package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.LoginActivity;
import com.hfad.taskmanager.model.User;
import com.hfad.taskmanager.repository.UserDBRepository;

public class SignUpFragment extends Fragment {
    private UserDBRepository mUserDBRepository;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButtonSignUp;
    private CheckBox mCheckBoxAdmin;


    public SignUpFragment() {
        // Required empty public constructor
    }


    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDBRepository = UserDBRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        findAllViews(view);
        setonClickListeners();

        return view;
    }

    private void setonClickListeners() {
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextUsername.getText().length() > 0 && mEditTextPassword.getText().length() > 0) {
                    User user;
                    if (mCheckBoxAdmin.isChecked())
                        user = new User(mEditTextUsername.getText().toString(), mEditTextPassword.getText().toString(), 1);
                    else
                        user = new User(mEditTextUsername.getText().toString(), mEditTextPassword.getText().toString(), 0);
                    Toast.makeText(getActivity(), "User added", Toast.LENGTH_LONG).show();
                    mUserDBRepository.insert(user);
                    getActivity().finish();
/*                    Intent intent = LoginActivity.newIntent(getActivity());
                    startActivity(intent);*/
                } else
                    Toast.makeText(getActivity(), "Please fill username and password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findAllViews(View view) {
        mEditTextUsername = view.findViewById(R.id.editText_username_sign_up);
        mEditTextPassword = view.findViewById(R.id.editText_password_sign_up);
        mButtonSignUp = view.findViewById(R.id.button_sign_up);
        mCheckBoxAdmin = view.findViewById(R.id.checkBox_admin);
    }
}