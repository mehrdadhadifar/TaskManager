package com.hfad.taskmanager.model;

import com.hfad.taskmanager.utils.Utils;

import java.util.UUID;

public class Task {
    private UUID mID;
    private State mState;
    private String mTitle;

    public Task() {
        mID = UUID.randomUUID();
    }

    public Task(String title) {
        this();
        mTitle = title;
        mState = Utils.randomEnum(State.class);
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "mID=" + mID +
                ", mState=" + mState +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }
}
