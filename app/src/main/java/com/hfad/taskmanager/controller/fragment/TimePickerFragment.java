package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.hfad.taskmanager.R;

import java.util.Calendar;
import java.util.Date;


public class TimePickerFragment extends DialogFragment {
    public static final String ARG_DATE = "ARG_DATE";
    public static final String EXTRA_USER_SELECTED_DATE = "EXTRA_USER_SELECTED_DATE";
    private TimePicker mTimePicker;
    private Date mCurrentDate;
    private Calendar mCalendar = Calendar.getInstance();


    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date currentDate) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, currentDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentDate = (Date) getArguments().getSerializable(ARG_DATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null);
        findAllViews(view);
        initTimePicker();
        return new AlertDialog.Builder(getActivity())
                .setTitle("Select Time:")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Date datePicked = getSelectedTimeFromTimePicker();
                        setResult(datePicked);
                    }
                })
                .create();
    }

    private void setResult(Date datePicked) {
        Fragment fragment = getTargetFragment();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_DATE, datePicked);
        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Date getSelectedTimeFromTimePicker() {
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        mCalendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, hour, minute);
        return mCalendar.getTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initTimePicker() {
        mCalendar.setTime(mCurrentDate);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);
    }

    private void findAllViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker_task);
    }
}