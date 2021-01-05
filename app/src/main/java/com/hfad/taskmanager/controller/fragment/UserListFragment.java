package com.hfad.taskmanager.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.User;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;
import com.hfad.taskmanager.adapter.UserAdapter;

import java.util.List;

public class UserListFragment extends Fragment {
    public static final String ARG_USER_UUID = "ARG_USER_UUID";
    private RecyclerView mRecyclerViewUsers;
    private UserDBRepository mUserDBRepository;
    private TaskDBRepository mTaskDBRepository;
    private UserAdapter mAdapter;
    /*    private TextView[] mTextViewsUsers;
        private LinearLayout mLinearLayoutMain;
        private Button mButtonDelete;
        private LinearLayout mLinearLayoutRow[];*/
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
        mTaskDBRepository = TaskDBRepository.getInstance(getActivity());
        mUserId = getArguments().getLong(ARG_USER_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        findAllViews(view);
        updateUI();


        return view;
    }

    private void findAllViews(View view) {
        mRecyclerViewUsers = view.findViewById(R.id.user_list_recycler_view);
    }

    private void updateUI() {
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<User> users = mUserDBRepository.getList();
        if (mAdapter == null) {
            mAdapter = new UserAdapter(users, getActivity());
            mRecyclerViewUsers.setAdapter(mAdapter);
        } else {
            mAdapter.setList(users);
            mAdapter.notifyDataSetChanged();
        }
    }


}