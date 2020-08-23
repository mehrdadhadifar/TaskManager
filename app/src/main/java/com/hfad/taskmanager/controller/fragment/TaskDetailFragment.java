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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class TaskDetailFragment extends DialogFragment {
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;
    public static final String DIALOG_DATE_FRAGMENT_TAG = "DIALOG_DATE_FRAGMENT_TAG";
    public static final String DIALOG_TIME_FRAGMENT_TAG = "DIALOG_TIME_FRAGMENT_TAG";
    public static final String ARG_TASK_ID = "ARG_TASK_ID";
    private EditText mEditTextTile;
    private EditText mEditTextComment;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupState;
    private Task mTask;
    private TaskRepository mTaskRepository;
    private Calendar mCalendar;


    public TaskDetailFragment() {
        // Required empty public constructor
    }


    public static TaskDetailFragment newInstance(UUID uuid) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskRepository = TaskRepository.getInstance();
        mCalendar = Calendar.getInstance();
        if (getArguments().getSerializable(ARG_TASK_ID) == null) {
            mTask = new Task("", State.Todo, new Date());
        } else
            mTask = mTaskRepository.get((UUID) getArguments().getSerializable(ARG_TASK_ID));
//        mTaskDate = new Date();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_show_task_details, null);
        findAllViews(view);
        updateUI();
        setonClickListeners();
        if (getArguments().getSerializable(ARG_TASK_ID) == null)
            return new AlertDialog.Builder(getActivity())
                    .setTitle("New Task")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextTile.getText().length() > 0)
                                mTaskRepository.insert(buildTask());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
        else
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Task Details")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextTile.getText().length() > 0)
                                mTaskRepository.update(buildTask());
                        }
                    })
                    .create();
    }

    private void updateTask() {

    }

    private void updateUI() {
        mEditTextTile.setText(mTask.getTitle());
        mEditTextComment.setText(mTask.getComment());
        mCalendar.setTime(mTask.getDate());
        mButtonDate.setText(mCalendar.get(Calendar.YEAR) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.DAY_OF_MONTH));
        mButtonTime.setText(mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND));
        switch (mTask.getState()) {
            case Todo:
                mRadioGroupState.check(R.id.todo_radio_button);
                break;
            case Done:
                mRadioGroupState.check(R.id.done_radio_button);
                break;
            default:
                mRadioGroupState.check(R.id.doing_radio_button);
                break;
        }
    }

    private void setonClickListeners() {
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mTask.getDate());
                datePickerFragment.setTargetFragment(TaskDetailFragment.this, DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mTask.getDate());
                timePickerFragment.setTargetFragment(TaskDetailFragment.this, TIME_PICKER_REQUEST_CODE);
                timePickerFragment.show(getFragmentManager(), DIALOG_TIME_FRAGMENT_TAG);
            }
        });
        mEditTextTile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTask.setTitle(mEditTextTile.getText().toString());
            }
        });
        mEditTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTask.setComment(mEditTextComment.getText().toString());
            }
        });
        mRadioGroupState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.todo_radio_button:
                        mTask.setState(State.Todo);
                        break;
                    case R.id.doing_radio_button:
                        mTask.setState(State.Doing);
                        break;
                    default:
                        mTask.setState(State.Done);
                        break;
                }
            }
        });
    }

    private Task buildTask() {
        mTask.setTitle(mEditTextTile.getText().toString());
        mTask.setComment(mEditTextComment.getText().toString());
        switch (mRadioGroupState.getCheckedRadioButtonId()) {
            case R.id.todo_radio_button:
                mTask.setState(State.Todo);
                break;
            case R.id.doing_radio_button:
                mTask.setState(State.Doing);
                break;
            default:
                mTask.setState(State.Done);
                break;
        }
        return mTask;
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
            mTask.setDate(mCalendar.getTime());
            updateUI();
        }
        if (requestCode == TIME_PICKER_REQUEST_CODE) {
            Date userSelectedDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_DATE);
            Calendar userSelectedCalender = Calendar.getInstance();
            userSelectedCalender.setTime(userSelectedDate);
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), userSelectedCalender.get(Calendar.HOUR), userSelectedCalender.get(Calendar.MINUTE));
            mTask.setDate(mCalendar.getTime());
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