package com.hfad.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hfad.taskmanager.model.User;

import java.util.List;

@Dao
public interface UserDataBaseDAO {
    @Insert
    void insertUser(User user);
    @Insert
    void insertUsers(User... users);

    @Query("SELECT * FROM UserTable WHERE id=:id")
    User getUser(long id);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM UserTable")
    List<User> getUsers();
}
