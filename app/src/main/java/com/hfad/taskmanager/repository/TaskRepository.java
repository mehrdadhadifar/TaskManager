package com.hfad.taskmanager.repository;

import com.hfad.taskmanager.model.Task;

import java.util.List;
import java.util.UUID;

public class TaskRepository implements IRepository {
    private static TaskRepository sTaskRepository;


    public static TaskRepository getInstance() {
        if (sTaskRepository == null)
            sTaskRepository = new TaskRepository();
        return sTaskRepository;
    }

    private List<Task> mTasks;

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }


    public List<Task> getTasks() {
        return mTasks;
    }

    private TaskRepository() {
    }

    @Override
    public List getList() {
        return null;
    }

    @Override
    public Object get(UUID uuid) {
        return null;
    }

    @Override
    public void setList(List list) {

    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public void insert(Object o) {

    }

    @Override
    public void insertList(List list) {

    }

    @Override
    public int getPosition(UUID uuid) {
        return 0;
    }
}
