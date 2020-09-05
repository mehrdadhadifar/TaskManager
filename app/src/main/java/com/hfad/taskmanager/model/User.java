package com.hfad.taskmanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "UserTable")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "username")
    private String mUsername;
    @ColumnInfo(name = "password")
    private String mPassword;
    @ColumnInfo(name = "role")
    private int mRole;
    //    private List<Task> mUserTaskList;
    @ColumnInfo(name = "registerDate")
    private Date mRegisterDate;


    public User(String username, String password, int role) {
        mRegisterDate=new Date();
        mUsername = username;
        mPassword = password;
        mRole = role;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getRegisterDate() {
        return mRegisterDate;
    }

    public void setRegisterDate(Date registerDate) {
        mRegisterDate = registerDate;
    }
}
