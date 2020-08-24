package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.SignUpActivity;
import com.hfad.taskmanager.controller.activity.TaskPagerActivity;
import com.hfad.taskmanager.repository.UserRepository;


public class LoginFragment extends Fragment {
    private UserRepository mUserRepository;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonSignUp;
    private CheckBox mCheckBoxAdmin;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserRepository = UserRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findAllViews(view);
        setonClickListeners();


        return view;
    }

    private void setonClickListeners() {
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i;
                for (i = 0; i < mUserRepository.getList().size(); i++) {
                    if (mUserRepository.getList().get(i).getUsername().equals(mEditTextUsername.getText().toString())
                            && mUserRepository.getList().get(i).getPassword().equals(mEditTextPassword.getText().toString())) {
                        Intent intent = TaskPagerActivity.newIntent(getActivity(), mUserRepository.getList().get(i).getUUID());
                        startActivity(intent);
                        Toast.makeText(getActivity(), mUserRepository.getList().get(i).getUsername() + " Welcome", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                if (i == mUserRepository.getList().size())
                    Toast.makeText(getActivity(), "Inputs are not valid!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findAllViews(View view) {
        mEditTextUsername = view.findViewById(R.id.editText_username_login);
        mEditTextPassword = view.findViewById(R.id.editText_password_login);
        mCheckBoxAdmin = view.findViewById(R.id.checkBox_admin);
        mButtonLogin = view.findViewById(R.id.button_login);
        mButtonSignUp = view.findViewById(R.id.button_sign_up);
    }
}