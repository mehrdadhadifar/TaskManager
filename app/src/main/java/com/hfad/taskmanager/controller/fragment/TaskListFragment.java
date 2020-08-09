package com.hfad.taskmanager.controller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.IRepository;
import com.hfad.taskmanager.repository.TaskRepository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String TAG = "TLF";
    private static final String ARG_STATES = "ARG_USERNAME";
    private RecyclerView mRecyclerViewTasks;
    private IRepository<Task> mRepository;
    private TaskAdapter mTaskAdapter;
    private Button mButtonAddNewTask;
    private List<State> mStateList;

    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(List<State> stateList) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATES, (Serializable) stateList);
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setArguments(args);
        return taskListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
    }

    private void initList() {
        mStateList = (List<State>) getArguments().getSerializable(ARG_STATES);
        mRepository = TaskRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findAllViews(view);
        updateList();
        setClickListener();
        return view;
    }

    private void setClickListener() {
        mButtonAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task("New Task");
                mRepository.insert(task);
                mTaskAdapter.notifyItemInserted(mRepository.getPosition(task.getID()));

//                Log.d(TAG + " id", String.valueOf(mRepository.getPosition(task.getID())));
//                Log.d(TAG + " pos in list", String.valueOf(getStateListPosition(task.getID())));
//                Log.d(TAG + " count", String.valueOf(mTaskAdapter.getItemCount()));
//                Log.d(TAG + " size", String.valueOf(mRepository.getListByStates(mStateList).size()));
            }
        });
    }

    private int getStateListPosition(UUID uuid) {
        TaskRepository taskRepository = (TaskRepository) mRepository;
        for (int i = 0; i < taskRepository.getListByStates(mStateList).size(); i++) {
            if (uuid.equals(taskRepository.getListByStates(mStateList).get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    private void updateList() {
        mRecyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Task> tasks = mRepository.getList();
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getListByStates(mStateList).size()));
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getList().size()));
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mRecyclerViewTasks.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewState;
        private LinearLayout mLinearLayoutMain;
        private ConstraintLayout mConstraintLayoutNothing;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.recycle_view_tasks_text_view_title);
            mTextViewState = itemView.findViewById(R.id.recycle_view_tasks_text_view_state);
            mLinearLayoutMain = itemView.findViewById(R.id.recycle_view_tasks_main_linear_layout);
            mConstraintLayoutNothing = itemView.findViewById(R.id.nothing_recycle_view_tasks);
        }

        public void bindTask(Task task) {
            if (getItemViewType() == 1) {
                mTextViewTitle.setText(task.getTitle());
                mTextViewState.setText(task.getState().toString());
                if (getStateListPosition(task.getID()) % 2 == 1)
                    mLinearLayoutMain.setBackgroundColor(Color.GRAY);
                else
                    mLinearLayoutMain.setBackgroundColor((Color.WHITE));
            }
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        List<Task> mTasks;

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @Override
        public int getItemCount() {
            Log.d(TAG + " size adapteram", String.valueOf(mTasks.size()));
            return mTasks.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mStateList.contains(mTasks.get(position).getState()))
                return 1;
            else
                return 0;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view;
            if (viewType == 1)
                view = inflater.inflate(R.layout.recycler_view_list_row_task, parent, false);
            else
                view = inflater.inflate(R.layout.recycler_view_list_row_nothing, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bindTask(task);
        }

    }

    private void findAllViews(View view) {
        mRecyclerViewTasks = view.findViewById(R.id.recycle_view_tasks);
        mButtonAddNewTask = view.findViewById(R.id.button_add_task);
    }
}