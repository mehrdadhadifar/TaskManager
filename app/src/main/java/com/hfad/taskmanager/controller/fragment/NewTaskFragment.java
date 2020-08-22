package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//TODO : Handel configuration change mTaskDate

public class NewTaskFragment extends DialogFragment {
    public static final int DATE_PICKER_REQUEST_CODE = 1;
    public static final String DIALOG_DATE_FRAGMENT_TAG = "DIALOG_DATE_FRAGMENT_TAG";
    private EditText mEditTextTile;
    private EditText mEditTextComment;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupState;
    private String mTaskTitle;
    private String mTaskComment;
    private State mTaskState;
    private Date mTaskDate;
    private TaskRepository mTaskRepository;
    private Calendar mCalendar;


    public NewTaskFragment() {
        // Required empty public constructor
    }


    public static NewTaskFragment newInstance() {
        NewTaskFragment fragment = new NewTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskDate = new Date();
        mTaskRepository = TaskRepository.getInstance();
        mCalendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_new_task, null);
        findAllViews(view);
        updateUI();
        setonClickListeners();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mEditTextTile.getText().length() > 0)
                            buildTask();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    private void updateUI() {
        mCalendar.setTime(mTaskDate);
        mButtonDate.setText(mCalendar.get(Calendar.YEAR) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.DAY_OF_MONTH));
        mButtonTime.setText(mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND));
    }

    private void setonClickListeners() {
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mTaskDate);
                datePickerFragment.setTargetFragment(NewTaskFragment.this, DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE_FRAGMENT_TAG);
            }
        });
    }

    private void buildTask() {
        mTaskTitle = mEditTextTile.getText().toString();
        mTaskComment = mEditTextComment.getText().toString();
        switch (mRadioGroupState.getCheckedRadioButtonId()) {
            case R.id.todo_radio_button:
                mTaskState = State.Todo;
                break;
            case R.id.doing_radio_button:
                mTaskState = State.Doing;
                break;
            default:
                mTaskState = State.Done;
                break;
        }
        Task task = new Task(mTaskState, mTaskTitle, mTaskComment, mTaskDate);
        mTaskRepository.insert(task);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            Date userSelectedDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            Calendar userSelectedCalendar = Calendar.getInstance();
            userSelectedCalendar.setTime(userSelectedDate);
            mCalendar.set(userSelectedCalendar.get(Calendar.YEAR), userSelectedCalendar.get(Calendar.MONTH), userSelectedCalendar.get(Calendar.DAY_OF_MONTH));
            mTaskDate = mCalendar.getTime();
            updateUI();
        }

    }

    private void findAllViews(View view) {
        mEditTextTile = view.findViewById(R.id.title_new_task_edit_text);
        mEditTextComment = view.findViewById(R.id.description_new_task_edit_text);
        mButtonDate = view.findViewById(R.id.date_button_new_task);
        mButtonTime = view.findViewById(R.id.time_button_new_task);
        mRadioGroupState = view.findViewById(R.id.radio_group_new_task_state);

    }
}