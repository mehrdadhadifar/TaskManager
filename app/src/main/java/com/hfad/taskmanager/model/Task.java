package com.hfad.taskmanager.model;

import com.hfad.taskmanager.utils.Utils;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID mID;
    private State mState;
    private String mTitle;
    private String mComment;
    private Date mDate;


    private Task() {
        mID = UUID.randomUUID();
    }

    public Task(String title) {
        this();
        mTitle = title;
        mState = Utils.randomEnum(State.class);
        mDate = Utils.getRandomDate();
    }

    public Task(String title, State state) {
        this();
        mTitle = title;
        mState = state;
        mDate = Utils.getRandomDate();
    }

    public Task(State state, String title, String comment) {
        this();
        mState = state;
        mTitle = title;
        mComment = comment;
        mDate = Utils.getRandomDate();
    }

    public Task(State state, String title, String comment, Date date) {
        this();
        mState = state;
        mTitle = title;
        mComment = comment;
        mDate = date;
    }

    public UUID getID() {
        return mID;
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

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
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
