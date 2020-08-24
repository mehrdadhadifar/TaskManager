package com.hfad.taskmanager.repository;

import com.hfad.taskmanager.model.State;
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

    public List<Task> getUserTasks(UUID userId) {
        List<Task> userTaskList = new ArrayList<>();
        UserRepository userRepository = UserRepository.getInstance();
        if (userRepository.get(userId).getRole() == 1)
            return mTasks;
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getUserId().equals(userId))
                userTaskList.add(mTasks.get(i));
        }
        return userTaskList;
    }

    private List<Task> mTasks;

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }


    public List<Task> getTasks() {
        return mTasks;
    }

    private TaskRepository() {
        mTasks = new ArrayList<>();
    }

    @Override
    public List<Task> getList() {
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
        updateTask.setComment(task.getComment());
        updateTask.setDate(task.getDate());
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
        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getID().equals(uuid))
                return i;
        }
        return -1;
    }

    public List<Task> getListByStates(List<State> stateList) {
        List<Task> tasksByStates = new ArrayList<>();
        for (int i = 0; i < mTasks.size(); i++) {
            for (int j = 0; j < stateList.size(); j++) {
                if (mTasks.get(i).getState().equals(stateList.get(j))) {
                    tasksByStates.add(mTasks.get(i));
                    break;
                }
            }
        }
        return tasksByStates;
    }
}
