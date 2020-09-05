package com.hfad.taskmanager.repository;

import android.content.Context;

import androidx.room.Room;

import com.hfad.taskmanager.database.UserDataBase;
import com.hfad.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

public class UserDBRepository implements IRepository<User> {
    private static UserDBRepository mUserDBRepository;
    private UserDataBase mUserDataBase;
    private static Context mContext;

    public static UserDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (mUserDBRepository == null)
            mUserDBRepository = new UserDBRepository();
        return mUserDBRepository;
    }

    private UserDBRepository() {
        mUserDataBase = Room.databaseBuilder(mContext
                , UserDataBase.class
                , "UserDB")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    public List<User> getList() {
        return mUserDataBase.UserDOA().getUsers();
    }

    @Override
    public User get(UUID uuid) {
        return null;
    }

    public User get(long id) {
        return mUserDataBase.UserDOA().getUser(id);
    }

    @Override
    public void setList(List<User> list) {
        User[] itemArray = new User[list.size()];
        mUserDataBase.UserDOA().insertUsers(list.toArray(itemArray));
    }

    @Override
    public void update(User user) {
        mUserDataBase.UserDOA().updateUser(user);
    }

    @Override
    public void delete(User user) {
        mUserDataBase.UserDOA().deleteUser(user);
    }

    @Override
    public void insert(User user) {
        mUserDataBase.UserDOA().insertUser(user);
    }

    @Override
    public void insertList(List<User> list) {
        List<User> users = mUserDataBase.UserDOA().getUsers();
        for (User user : list
        ) {
            users.add(user);
        }
        insertList(users);
    }

    @Override
    public int getPosition(UUID uuid) {
        return -1;
    }

    public int getPosition(long id) {
        List<User> users = getList();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id)
                return i;
        }
        return -1;
    }
}
