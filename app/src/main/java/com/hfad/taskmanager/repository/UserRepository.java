package com.hfad.taskmanager.repository;

import com.hfad.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository implements IRepository<User> {
    private static UserRepository mUserRepository;
    private List<User> mUsers;

    public static UserRepository getInstance() {
        if (mUserRepository == null)
            mUserRepository = new UserRepository();
        return mUserRepository;
    }

    private UserRepository() {
        mUsers = new ArrayList<>();
    }

    @Override
    public List<User> getList() {
        return mUsers;
    }

    @Override
    public User get(UUID uuid) {
        for (User user : mUsers
        ) {
            if (user.getUUID().equals(uuid))
                return user;
        }
        return null;
    }

    @Override
    public void setList(List<User> list) {
        mUsers = list;
    }

    @Override
    public void update(User user) {
        User updateUser = get(user.getUUID());
        updateUser.setPassword(user.getPassword());
        updateUser.setUsername(user.getUsername());
        updateUser.setRole(user.getRole());
    }

    @Override
    public void delete(User user) {
        for (int i = 0; i < mUsers.size(); i++) {
            if (mUsers.get(i).getUUID() == user.getUUID()) {
                mUsers.remove(i);
                break;
            }
        }
    }

    @Override
    public void insert(User user) {
        mUsers.add(user);
    }

    @Override
    public void insertList(List<User> list) {
        for (User user : list
        ) {
            mUsers.add(user);
        }
    }

    @Override
    public int getPosition(UUID uuid) {
        for (int i = 0; i < mUsers.size(); i++) {
            if (mUsers.get(i).getUUID().equals(uuid))
                return i;
        }
        return -1;
    }
}
