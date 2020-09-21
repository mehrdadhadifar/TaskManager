package com.hfad.taskmanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hfad.taskmanager.database.utils.DBConverters;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.model.User;

@Database(entities = {Task.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({DBConverters.UUIDConverter.class, DBConverters.DateConverters.class, DBConverters.EnumConverter.class})
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskDataBaseDAO TaskDOA();

    public abstract UserDataBaseDAO UserDOA();
}
