package com.hfad.taskmanager.controller.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.TaskPagerActivity;
import com.hfad.taskmanager.model.User;
import com.hfad.taskmanager.repository.UserDBRepository;

import java.util.List;
import java.util.UUID;

public class UserListFragment extends Fragment {
    public static final String ARG_USER_UUID = "ARG_USER_UUID";
    private UserDBRepository mUserDBRepository;
    private TextView[] mTextViewsUsers;
    private LinearLayout mLinearLayoutMain;
    private Button mButtonDelete;
    private LinearLayout mLinearLayoutRow[];
    private long mUserId;


    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(long userId) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_UUID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDBRepository = UserDBRepository.getInstance(getActivity());
        mUserId = getArguments().getLong(ARG_USER_UUID);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mLinearLayoutMain = view.findViewById(R.id.main_scroll_view);
        final List<User> users = mUserDBRepository.getList();
        mTextViewsUsers = new TextView[users.size()];
        mLinearLayoutRow = new LinearLayout[users.size()];
        int i;
        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == mUserId)
                continue;
            mLinearLayoutRow[i] = new LinearLayout(getActivity());
            mLinearLayoutRow[i].setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayoutMain.addView(mLinearLayoutRow[i]);
            mTextViewsUsers[i] = new TextView(getActivity());
            mTextViewsUsers[i].setText(users.get(i).getUsername());
            mTextViewsUsers[i].setVisibility(View.VISIBLE);
            mTextViewsUsers[i].setTextSize(32);
            mTextViewsUsers[i].setGravity(View.TEXT_ALIGNMENT_CENTER);
            mTextViewsUsers[i].setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
            mTextViewsUsers[i].setWidth(500);
            mLinearLayoutRow[i].addView(mTextViewsUsers[i]);
            mButtonDelete = new Button(getActivity());
            mButtonDelete.setText("Delete");
            mButtonDelete.setForegroundGravity(View.SCROLL_INDICATOR_END);
            mLinearLayoutRow[i].addView(mButtonDelete);
            final int finalI = i;
            final int finalI1 = i;
            mButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUserDBRepository.delete(users.get(finalI));
                    mLinearLayoutRow[finalI1].setVisibility(View.GONE);
                }
            });
            final int finalI2 = i;
            mTextViewsUsers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = TaskPagerActivity.newIntent(getActivity(), users.get(finalI2).getId());
                    startActivity(intent);
                }
            });
        }


        return view;
    }
}