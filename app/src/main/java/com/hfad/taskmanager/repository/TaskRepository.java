package com.hfad.taskmanager.repository;

import com.hfad.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepository implements IRepository<Task> {
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
        mTasks=new ArrayList<Task>();
    }

    @Override
    public List getList() {
        return mTasks;
    }

    @Override
    public Task get(UUID uuid) {
        for (Task task : mTasks
        ) {
            if (task.getID() == uuid)
                return task;
        }
        return null;
    }

    @Override
    public void update(Task task) {
        Task updateTask = get(task.getID());
        updateTask.setState(task.getState());
        updateTask.setTitle(task.getTitle());
    }

    @Override
    public void delete(Task task) {
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getID() == task.getID()) {
                mTasks.remove(i);
                break;
            }
        }
    }

    @Override
    public void insert(Task task) {
        mTasks.add(task);
    }

    @Override
    public void setList(List<Task> list) {
        mTasks = list;
    }


    @Override
    public void insertList(List<Task> list) {
        for (Task task : list
        ) {
            mTasks.add(task);
        }
    }

    @Override
    public int getPosition(UUID uuid) {
        return 0;
    }
}
