package com.hfad.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hfad.taskmanager.model.Task;

import java.util.List;
@Dao
public interface TaskDataBaseDAO {
    @Insert
    void insertTask(Task task);
    @Insert
    void insertTasks(Task... tasks);
    @Update
    void updateTask(Task task);
    @Delete
    void deleteTask(Task task);
    @Query("SELECT * FROM TASKTABLE WHERE uuid=:uuid")
    Task getTask(String uuid);
    @Query("SELECT * FROM TASKTABLE")
    List<Task> getTasks();

}
