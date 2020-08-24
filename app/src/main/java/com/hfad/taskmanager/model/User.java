package com.hfad.taskmanager.model;

import com.hfad.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

public class User {
    private UUID mUUID;
    private String mUsername;
    private String mPassword;
    private int mRole;
    private List<Task> mUserTaskList;

    private User() {
        mUUID = UUID.randomUUID();
    }

    public User(String username, String password, int role) {
        this();
        mUsername = username;
        mPassword = password;
        mRole = role;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getRole() {
        return mRole;
    }

    public void setRole(int role) {
        mRole = role;
    }

    public List<Task> getUserTaskList() {
        return mUserTaskList;
    }

    public void setUserTaskList(List<Task> userTaskList) {
        mUserTaskList = userTaskList;
    }
}
