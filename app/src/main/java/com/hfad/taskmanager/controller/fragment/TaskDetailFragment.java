package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.controller.activity.TaskPagerActivity;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class TaskDetailFragment extends DialogFragment {
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;
    public static final String DIALOG_DATE_FRAGMENT_TAG = "DIALOG_DATE_FRAGMENT_TAG";
    public static final String DIALOG_TIME_FRAGMENT_TAG = "DIALOG_TIME_FRAGMENT_TAG";
    public static final String ARG_TASK_ID = "ARG_TASK_ID";
    public static final String ARG_USER_ID = "ARG_USER_ID";
    private EditText mEditTextTile;
    private EditText mEditTextComment;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupState;
    private Task mUpdateTask;
    private Task mTask;
    private TaskDBRepository mTaskRepository;
    private Calendar mCalendar;
    private boolean mUnEditable;
    private long mUserId;
    private UserDBRepository mUserDBRepository;
    private Callbacks mCallbacks;


    public TaskDetailFragment() {
        // Required empty public constructor
    }


    public static TaskDetailFragment newInstance(UUID taskId, long userId) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        args.putSerializable(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Callbacks)
            mCallbacks= (Callbacks) context;
        else
            throw new ClassCastException(context.toString()
                    + " must implement onCrimeUpdated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskRepository = TaskDBRepository.getInstance(getActivity());
        mUserDBRepository = UserDBRepository.getInstance(getActivity());
        mCalendar = Calendar.getInstance();
        if (getArguments().getSerializable(ARG_TASK_ID) == null) {
            mUpdateTask = new Task("", State.Todo);
        } else {
            mTask = mTaskRepository.get((UUID) getArguments().getSerializable(ARG_TASK_ID));
            mUpdateTask = new Task(mTask.getTitle(), mTask.getState(), mTask.getComment(), mTask.getDate());
            mUnEditable = true;
        }
        mUserId = getArguments().getLong(ARG_USER_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_show_task_details, null);
        findAllViews(view);
        updateUI();
        setonClickListeners();
        final AlertDialog dialog;
        if (getArguments().getSerializable(ARG_TASK_ID) == null)
            dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("New Task")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextTile.getText().length() > 0) {
                                Task task = buildTask();
                                mTaskRepository.insert(task);
                                mCallbacks.onTaskUpdate();
//                                ((TaskPagerActivity) getActivity()).updateUI();
                            } else
                                Toast.makeText(getActivity(), "Task title must not be empty!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
        else {
            dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Task Details")
                    .setView(view)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextTile.getText().length() > 0) {
                                mUpdateTask.setId(mTask.getId());
                                mTaskRepository.update(buildTask());
                                mCallbacks.onTaskUpdate();
//                                ((TaskPagerActivity) getActivity()).setUI();
                            } else
                                Toast.makeText(getActivity(), "Task title must not be empty!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteTask();
                            Toast.makeText(getActivity(), "Your task has been deleted!!", Toast.LENGTH_LONG).show();
                            mCallbacks.onTaskUpdate();
//                            ((TaskPagerActivity) getActivity()).setUI();
                        }
                    })
                    .setNeutralButton("Edit", null)
                    .create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUnEditable = !mUnEditable;
                            mEditTextTile.setEnabled(true);
                            mEditTextComment.setEnabled(true);
                            mButtonDate.setEnabled(true);
                            mButtonTime.setEnabled(true);
                            for (int i = 0; i < mRadioGroupState.getChildCount(); i++) {
                                (mRadioGroupState.getChildAt(i)).setEnabled(true);
                            }
                        }
                    });
                }
            });
        }
        return dialog;
    }

    private void deleteTask() {
        mUpdateTask.setId(mTask.getId());
        mTaskRepository.delete(mUpdateTask);
    }

    private void updateUI() {
        mEditTextTile.setText(mUpdateTask.getTitle());
        mEditTextComment.setText(mUpdateTask.getComment());
        mCalendar.setTime(mUpdateTask.getDate());
        mButtonDate.setText(mCalendar.get(Calendar.YEAR) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.DAY_OF_MONTH));
        mButtonTime.setText(mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND));
        switch (mUpdateTask.getState()) {
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
        if (mUnEditable) {
            mEditTextTile.setEnabled(false);
            mEditTextComment.setEnabled(false);
            mButtonDate.setEnabled(false);
            mButtonTime.setEnabled(false);
            for (int i = 0; i < mRadioGroupState.getChildCount(); i++) {
                (mRadioGroupState.getChildAt(i)).setEnabled(false);
            }
        }
    }

    private void setonClickListeners() {
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mUpdateTask.getDate());
                datePickerFragment.setTargetFragment(TaskDetailFragment.this, DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mUpdateTask.getDate());
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
                mUpdateTask.setTitle(mEditTextTile.getText().toString());
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
                mUpdateTask.setComment(mEditTextComment.getText().toString());
            }
        });
        mRadioGroupState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.todo_radio_button:
                        mUpdateTask.setState(State.Todo);
                        break;
                    case R.id.doing_radio_button:
                        mUpdateTask.setState(State.Doing);
                        break;
                    default:
                        mUpdateTask.setState(State.Done);
                        break;
                }
            }
        });
    }

    private Task buildTask() {
        mUpdateTask.setTitle(mEditTextTile.getText().toString());
        mUpdateTask.setComment(mEditTextComment.getText().toString());
        switch (mRadioGroupState.getCheckedRadioButtonId()) {
            case R.id.todo_radio_button:
                mUpdateTask.setState(State.Todo);
                break;
            case R.id.doing_radio_button:
                mUpdateTask.setState(State.Doing);
                break;
            default:
                mUpdateTask.setState(State.Done);
                break;
        }
        if (mUserDBRepository.get(mUserId).getRole() == 0)
            mUpdateTask.setUserId(mUserId);
        else
            mUpdateTask.setUserId(mTask.getUserId());
        return mUpdateTask;
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
            mUpdateTask.setDate(mCalendar.getTime());
            updateUI();
        }
        if (requestCode == TIME_PICKER_REQUEST_CODE) {
            Date userSelectedDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_DATE);
            Calendar userSelectedCalender = Calendar.getInstance();
            userSelectedCalender.setTime(userSelectedDate);
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), userSelectedCalender.get(Calendar.HOUR), userSelectedCalender.get(Calendar.MINUTE));
            mUpdateTask.setDate(mCalendar.getTime());
            updateUI();
        }
    }

    public interface Callbacks {
        void onTaskUpdate();
    }

    private void findAllViews(View view) {
        mEditTextTile = view.findViewById(R.id.title_new_task_edit_text);
        mEditTextComment = view.findViewById(R.id.description_new_task_edit_text);
        mButtonDate = view.findViewById(R.id.date_button_new_task);
        mButtonTime = view.findViewById(R.id.time_button_new_task);
        mRadioGroupState = view.findViewById(R.id.radio_group_new_task_state);
    }
}