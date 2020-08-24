package com.hfad.taskmanager.model;

import com.hfad.taskmanager.repository.TaskRepository;

import java.util.UUID;

public class User {
    private UUID mUUID;
    private String mUsername;
    private String mPassword;

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

    public TaskRepository getUserTaskRepository() {
        return mUserTaskRepository;
    }

    public void setUserTaskRepository(TaskRepository userTaskRepository) {
        mUserTaskRepository = userTaskRepository;
    }

    private int mRole;
    private TaskRepository mUserTaskRepository;


}
