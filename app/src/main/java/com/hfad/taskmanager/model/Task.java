package com.hfad.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.hfad.taskmanager.utils.Utils;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "TaskTable")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "uuid")
    private UUID mUUID;
    @ColumnInfo(name = "state")
    private State mState;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "comment")
    private String mComment;
    @ColumnInfo(name = "date")
    private Date mDate;
    @ColumnInfo(name = "userId")
    private long mUserId;
    @ColumnInfo(name = "photoFileName")
    private String mPhotoFileName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Task() {
        mUUID = UUID.randomUUID();
    }

    public Task(String title) {
        this();
        mTitle = title;
        mState = Utils.randomEnum(State.class);
        mDate = Utils.getRandomDate();
        mComment = "";
        mPhotoFileName = producePhotoFileName();
    }

    @Ignore
    public Task(String title, State state) {
        this();
        mTitle = title;
        mState = state;
        mDate = Utils.getRandomDate();
        mComment = "";
        mPhotoFileName = producePhotoFileName();
    }

    @Ignore
    public Task(String title, State state, String comment) {
        this();
        mState = state;
        mTitle = title;
        mComment = comment;
        mDate = Utils.getRandomDate();
        mPhotoFileName = producePhotoFileName();
    }

    @Ignore
    public Task(String title, State state, String comment, Date date) {
        this();
        mState = state;
        mTitle = title;
        mComment = comment;
        mDate = date;
        mPhotoFileName = producePhotoFileName();
    }

    @Ignore
    public Task(String title, State state, String comment, Date date, String photoFileName) {
        this();
        mState = state;
        mTitle = title;
        mComment = comment;
        mDate = date;
        mPhotoFileName = photoFileName;
    }

    @Ignore
    public Task(String title, State state, Date date) {
        this();
        mState = state;
        mTitle = title;
        mDate = date;
        mComment = "";
        mPhotoFileName = producePhotoFileName();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID uuid) {
        mUUID = uuid;
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

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public String getPhotoFileName() {
        return mPhotoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        mPhotoFileName = photoFileName;
    }

    public String producePhotoFileName() {
        return "IMG_" + getDate() + ".jpg";
    }

    @Override
    public String toString() {
        return "Task{" +
                "mID=" + mUUID +
                ", mState=" + mState +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }

}
