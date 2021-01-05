package com.hfad.taskmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.TaskPagerActivity;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.model.User;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    public static final String TAG = "UA";
    List<User> mList;
    List<User> mListFull;
    private Context mContext;
    private UserDBRepository mUserDBRepository;
    private TaskDBRepository mTaskDBRepository;


    public UserAdapter(List<User> list, Context context) {
        mList = list;
        mListFull = new ArrayList<>(mList);
        mContext = context;
        mUserDBRepository = UserDBRepository.getInstance(mContext);
        mTaskDBRepository = TaskDBRepository.getInstance(mContext);
    }

    public List<User> getList() {
        return mList;
    }

    public void setList(List<User> list) {
        mList = list;
        mListFull = new ArrayList<>(mList);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_view_lisr_row_user, parent, false);
        return new UserHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = mList.get(position);
/*        if (user.getRole() == 1)
            return;*/
        holder.bindUser(user);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private ImageButton mImageButtonDelete;
        private TextView mTextViewUsername;
        private TextView mTextViewRegisterDate;
        private TextView mTextViewTasksNumber;
        private User mUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mImageButtonDelete = itemView.findViewById(R.id.delete_user);
            mTextViewRegisterDate = itemView.findViewById(R.id.register_date_text_view);
            mTextViewTasksNumber = itemView.findViewById(R.id.number_of_tasks);
            mTextViewUsername = itemView.findViewById(R.id.username_row_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = TaskPagerActivity.newIntent(mContext, mUser.getId());
                    mContext.startActivity(intent);
                }
            });
            mImageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Task> tasks = mTaskDBRepository.getUserTasks(mUser.getId());
                    for (Task task : tasks
                    ) {
                        mTaskDBRepository.delete(task);
                    }
                    mUserDBRepository.delete(mUser);
                    setList(mUserDBRepository.getList());
                    notifyDataSetChanged();
                }
            });
        }

        public void bindUser(User user) {
            mUser = user;
            mTextViewUsername.setText(user.getUsername());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getRegisterDate());
//            mTextViewRegisterDate.setText(DateUtils.formatDateTime(mContext, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_TIME));
            long now = System.currentTimeMillis();
            mTextViewRegisterDate.setText(DateUtils.getRelativeTimeSpanString(user.getRegisterDate().getTime(), now, DateUtils.DAY_IN_MILLIS));
            Log.d(TAG, String.valueOf(mTaskDBRepository.getUserTasks(user.getId()).size()));
            mTextViewTasksNumber.setText(String.valueOf(mTaskDBRepository.getUserTasks(user.getId()).size()));
        }

    }

}
