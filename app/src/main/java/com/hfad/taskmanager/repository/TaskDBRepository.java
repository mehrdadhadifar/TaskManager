package com.hfad.taskmanager.repository;

import android.content.Context;

import androidx.room.Room;

import com.hfad.taskmanager.database.TaskDataBase;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDBRepository implements IRepository<Task> {
    private static TaskDBRepository sTaskDBRepository;
    private static Context mContext;
    private TaskDataBase mTaskDataBase;


    public static TaskDBRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sTaskDBRepository == null)
            sTaskDBRepository = new TaskDBRepository();
        return sTaskDBRepository;
    }

    private TaskDBRepository() {
        mTaskDataBase = Room.databaseBuilder(mContext
                , TaskDataBase.class, "TaskDB")
                .allowMainThreadQueries()
                .build();
    }

    //Insert all
    @Override
    public void setList(List<Task> tasks) {
        Task[] itemArray = new Task[tasks.size()];
        mTaskDataBase.TaskDOA().insertTasks(tasks.toArray(itemArray));
    }

    //Read all
    @Override
    public List<Task> getList() {
        return mTaskDataBase.TaskDOA().getTasks();
    }

    //Get one
    @Override
    public Task get(UUID uuid) {
        return mTaskDataBase.TaskDOA().getTask(uuid.toString());
    }

    //Update task
    @Override
    public void update(Task task) {
        mTaskDataBase.TaskDOA().updateTask(task);
    }

    //Delete task
    @Override
    public void delete(Task task) {
        mTaskDataBase.TaskDOA().deleteTask(task);
    }

    //Insert one
    @Override
    public void insert(Task task) {
        mTaskDataBase.TaskDOA().insertTask(task);
    }

    @Override
    public void insertList(List<Task> list) {
        List<Task> tasks = mTaskDataBase.TaskDOA().getTasks();
        for (Task task : list
        ) {
            tasks.add(task);
        }
        insertList(tasks);
    }

    //Get position
    @Override
    public int getPosition(UUID uuid) {
        List<Task> tasks = getList();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getUUID().equals(uuid))
                return i;
        }
        return -1;
    }

    @Override
    public File getPhotoFile(Context context, Task task) {
        File photoFile = new File(context.getFilesDir(), task.getPhotoFileName());
        return photoFile;
    }

    //Get tasks for one user
    public List<Task> getUserTasks(long userId) {
        List<Task> userTaskList = new ArrayList<>();
        List<Task> allTasks = getList();
        UserDBRepository userDBRepository = UserDBRepository.getInstance(mContext);
        if (userDBRepository.get(userId).getRole() == 1)
            return allTasks;
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getUserId() == userId)
                userTaskList.add(allTasks.get(i));
        }
        return userTaskList;
    }

    //Get lists of tasks by states
    public List<Task> getListByStates(List<State> stateList) {
        List<Task> tasksByStates = new ArrayList<>();
        List<Task> tasks = getList();
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < stateList.size(); j++) {
                if (tasks.get(i).getState().equals(stateList.get(j))) {
                    tasksByStates.add(tasks.get(i));
                    break;
                }
            }
        }
        return tasksByStates;
    }

    public List<Task> getTasksByUserPerStates(long userId, List<State> stateList) {
        UserDBRepository userDBRepository = UserDBRepository.getInstance(mContext);
        if (userDBRepository.get(userId).getRole() == 1)
            return getListByStates(stateList);
        List<Task> tasks = getListByStates(stateList);
        List<Task> result = new ArrayList<>();
        for (Task task : tasks
        ) {
            if (task.getUserId() == userId)
                result.add(task);
        }
        return result;
    }
}
