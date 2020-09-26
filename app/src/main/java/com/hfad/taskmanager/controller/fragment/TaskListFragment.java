package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.utils.PictureUtils;
import com.ortiz.touchview.TouchImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class TaskListFragment extends Fragment {
    public static final String TAG = "TLF";
    private static final String ARG_STATES = "ARG_USERNAME";
    public static final int TASK_DETAIL_REQUEST_CODE = 0;
    public static final String TASK_DETAIL_FRAGMENT_TAG = "TASK_DETAIL_FRAGMENT_TAG";
    public static final String ARG_USER_UUID = "ARG_USER_UUID";
    private RecyclerView mRecyclerViewTasks;
    private LinearLayout mLinearLayoutEmpty;
    private TaskDBRepository mTaskRepository;
    private TaskAdapter mTaskAdapter;
    private List<State> mStateList;
    private long mUserId;
    private SearchView mSearchView;
    private Toolbar mToolbar;

    //
    public TaskListFragment() {
        // Required empty public constructor
    }

    public static TaskListFragment newInstance(List<State> stateList, long userId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATES, (Serializable) stateList);
        args.putSerializable(ARG_USER_UUID, userId);
        TaskListFragment taskListFragment = new TaskListFragment();
        taskListFragment.setArguments(args);
        return taskListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initList();
    }

    private void initList() {
        mStateList = (List<State>) getArguments().getSerializable(ARG_STATES);
        mUserId = getArguments().getLong(ARG_USER_UUID);
        mTaskRepository = TaskDBRepository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        findAllViews(view);
        updateUI();
        setClickListener();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.task_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_all:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Are sure to delete all tasks?")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Task> list = mTaskRepository.getUserTasks(mUserId);
                                for (int j = 0; j < list.size(); j++) {
                                    mTaskRepository.delete(list.get(j));
                                }
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create().show();
                return true;
            case R.id.menu_item_sign_out:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mTaskRepository = TaskDBRepository.getInstance(getActivity());
        Log.d(TAG, "on resume");
        Log.d(TAG, "size list select:" + mTaskRepository.getListByStates(mStateList).size());
        updateUI();
    }

    private void findAllViews(View view) {
        mRecyclerViewTasks = view.findViewById(R.id.recycle_view_tasks);
        mLinearLayoutEmpty = view.findViewById(R.id.empty_linear_layout);
        mToolbar = view.findViewById(R.id.toolbar_task_list);
        mSearchView = view.findViewById(R.id.search_view);
    }

    private void setClickListener() {
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setIconified(false);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mTaskAdapter.getFilter().filter(query);
                Log.d(TAG, "search change");
                return false;
            }
        });
    }

    private int getStateListPosition(UUID uuid) {
        for (int i = 0; i < mTaskRepository.getTasksByUserPerStates(mUserId, mStateList).size(); i++) {
            if (uuid.equals(mTaskRepository.getTasksByUserPerStates(mUserId, mStateList).get(i).getUUID())) {
                return i;
            }
        }
        return -1;
    }


    public void updateUI() {
        mRecyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Task> tasks = mTaskRepository.getTasksByUserPerStates(mUserId, mStateList);

        if (tasks.size() == 0) {
            mLinearLayoutEmpty.setVisibility(View.VISIBLE);
            mRecyclerViewTasks.setVisibility(View.GONE);
        } else {
            mLinearLayoutEmpty.setVisibility(View.GONE);
            mRecyclerViewTasks.setVisibility(View.VISIBLE);
        }
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getListByStates(mStateList).size()));
//        Log.d(TAG+" Sinze",String.valueOf(mRepository.getList().size()));
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mRecyclerViewTasks.setAdapter(mTaskAdapter);
        } else {
            Log.d(TAG, "Adapter Notify");
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private TextView mTextViewState;
        private LinearLayout mLinearLayoutMain;
        private TextView mTextViewComment;
        private TextView mTextViewDate;
        private Task mTask;
        private RoundedLetterView mRoundedLetterView;
        private ImageView mImageViewShowImage;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.recycle_view_tasks_text_view_title);
            mTextViewState = itemView.findViewById(R.id.recycle_view_tasks_text_view_state);
            mLinearLayoutMain = itemView.findViewById(R.id.recycle_view_tasks_main_linear_layout);
            mTextViewComment = itemView.findViewById(R.id.recycle_view_tasks_text_view_comment);
            mTextViewDate = itemView.findViewById(R.id.recycle_view_tasks_text_view_date);
            mRoundedLetterView = itemView.findViewById(R.id.rlv_name_view);
            mImageViewShowImage = itemView.findViewById(R.id.imageView_row_task_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogTaskDetailFragment dialogTaskDetailFragment = DialogTaskDetailFragment.newInstance(mTask.getUUID(), mUserId);
                    setTargetFragment(dialogTaskDetailFragment, TASK_DETAIL_REQUEST_CODE);
                    dialogTaskDetailFragment.show(getFragmentManager(), TASK_DETAIL_FRAGMENT_TAG);
                }
            });
        }

        public void bindTask(Task task) {
            mTask = task;
            if (getItemViewType() == 1) {
                mTextViewTitle.setText(task.getTitle());
                mTextViewState.setText(task.getState().toString());
                mTextViewComment.setText(task.getComment());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(task.getDate());
                mTextViewDate.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
//                mTextViewDate.setText(calendar.get(Calendar.MONTH));
                if (getStateListPosition(task.getUUID()) % 2 == 1)
                    mLinearLayoutMain.setBackgroundColor(getResources().getColor(R.color.lightPurple));
                else
                    mLinearLayoutMain.setBackgroundColor(getResources().getColor(R.color.white));
                if (mTaskRepository.getPhotoFile(getActivity(), task) != null && mTaskRepository.getPhotoFile(getActivity(), task).exists()) {
                    mRoundedLetterView.setVisibility(View.GONE);
                    Bitmap bitmap = PictureUtils.getScaledBitmap(mTaskRepository.getPhotoFile(getActivity(), task).getPath(), getActivity());
                    mImageViewShowImage.setImageBitmap(bitmap);
                    mImageViewShowImage.setVisibility(View.VISIBLE);
                } else {
                    mRoundedLetterView.setTitleText(String.valueOf(task.getTitle().charAt(0)));
                    mRoundedLetterView.setVisibility(View.VISIBLE);
                    mImageViewShowImage.setVisibility(View.GONE);
                }
            }
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> implements Filterable {
        List<Task> mTasks;
        List<Task> mTaskListFull;

        public List<Task> getTasks() {
            return mTasks;
        }

        public void setTasks(List<Task> tasks) {
            mTasks = tasks;
            mTaskListFull = new ArrayList<>(mTasks);
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
            mTaskListFull = new ArrayList<>(mTasks);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG + " size adapteram", String.valueOf(mTasks.size()));
            return mTasks.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mTaskListFull.size() == 0)
                return 0;
            else
                return 1;
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

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    List<Task> filteredTasks = new ArrayList<>();
                    if (charString.isEmpty()) {
                        filteredTasks.addAll(mTaskListFull);
                    } else {
                        for (Task task : mTaskListFull
                        ) {
                            if (task.getTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                    task.getComment().toLowerCase().contains(charString.toLowerCase()) ||
                                    task.getDate().toString().contains(charString.toLowerCase())) {
                                filteredTasks.add(task);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredTasks;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mTasks.clear();
                    mTasks.addAll((List) filterResults.values);
                    notifyDataSetChanged();
                }
            };
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null || resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == TASK_DETAIL_REQUEST_CODE) {
            updateUI();
        }
    }
}