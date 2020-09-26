package com.hfad.taskmanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.hfad.taskmanager.R;
import com.hfad.taskmanager.model.State;
import com.hfad.taskmanager.model.Task;
import com.hfad.taskmanager.repository.TaskDBRepository;
import com.hfad.taskmanager.repository.UserDBRepository;
import com.hfad.taskmanager.utils.PictureUtils;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class DialogTaskDetailFragment extends DialogFragment {
    public static final int DATE_PICKER_REQUEST_CODE = 0;
    public static final int TIME_PICKER_REQUEST_CODE = 1;
    public static final int IMAGE_CAPTURE_REQUEST_CODE = 2;
    public static final String DIALOG_DATE_FRAGMENT_TAG = "DIALOG_DATE_FRAGMENT_TAG";
    public static final String DIALOG_TIME_FRAGMENT_TAG = "DIALOG_TIME_FRAGMENT_TAG";
    public static final String ARG_TASK_ID = "ARG_TASK_ID";
    public static final String ARG_USER_ID = "ARG_USER_ID";
    public static final String FILE_PROVIDER_AUTHORITY = "com.hfad.taskmanager.controller.fileprovider";
    private EditText mEditTextTile;
    private EditText mEditTextComment;
    private Button mButtonDate;
    private Button mButtonTime;
    private RadioGroup mRadioGroupState;
    private ImageView mImageViewShowImage;
    private Button mButtonCapture;
    private Task mUpdateTask;
    private Task mTask;
    private TaskDBRepository mTaskRepository;
    private Calendar mCalendar;
    private boolean mUnEditable;
    private long mUserId;
    private UserDBRepository mUserDBRepository;
    private Callbacks mCallbacks;
    private File mPhotoFile;
    private ImageButton mImageButtonShare;


    public DialogTaskDetailFragment() {
        // Required empty public constructor
    }


    public static DialogTaskDetailFragment newInstance(UUID taskId, long userId) {
        DialogTaskDetailFragment fragment = new DialogTaskDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        args.putSerializable(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
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
            mUpdateTask = new Task(mTask.getTitle(), mTask.getState(), mTask.getComment(), mTask.getDate(), mTask.getPhotoFileName());
            mUnEditable = true;
        }
        mUserId = getArguments().getLong(ARG_USER_ID);
        mPhotoFile = mTaskRepository.getPhotoFile(getActivity(), mUpdateTask);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_dialog_show_task_details, null);
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
                                mTaskRepository.insert(buildTask());
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
                    final Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mUnEditable = !mUnEditable;
                            setonClickListeners();
                            mEditTextTile.setFocusableInTouchMode(true);
                            mEditTextComment.setFocusableInTouchMode(true);
                            for (int i = 0; i < mRadioGroupState.getChildCount(); i++) {
                                (mRadioGroupState.getChildAt(i)).setEnabled(true);
                            }
                            button.setEnabled(false);
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
            mEditTextTile.setFocusable(false);
            mEditTextComment.setFocusable(false);
            for (int i = 0; i < mRadioGroupState.getChildCount(); i++) {
                (mRadioGroupState.getChildAt(i)).setEnabled(false);
            }
        }
        updatePhoto();
    }

    private void setonClickListeners() {
        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUnEditable) {
                    return;
                }
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mUpdateTask.getDate());
                datePickerFragment.setTargetFragment(DialogTaskDetailFragment.this, DATE_PICKER_REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE_FRAGMENT_TAG);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUnEditable) {
                    return;
                }
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mUpdateTask.getDate());
                timePickerFragment.setTargetFragment(DialogTaskDetailFragment.this, TIME_PICKER_REQUEST_CODE);
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
        mButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUnEditable) {
                    return;
                }
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    if (mPhotoFile == null)
                        return;
                    Uri photoURI = FileProvider.getUriForFile(
                            getActivity(),
                            FILE_PROVIDER_AUTHORITY,
                            mPhotoFile
                    );
                    grantTemPermissionForTakePicture(takePictureIntent, photoURI);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
                }
            }
        });
        mImageViewShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoFile == null || !mPhotoFile.exists())
                    return;
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.layout_full_image);
                TouchImageView bmImage = (TouchImageView) dialog.findViewById(R.id.img_receipt);
                bmImage.setImageDrawable(Drawable.createFromPath(mPhotoFile.getPath()));
                Button button = (Button) dialog.findViewById(R.id.btn_dismiss);
                dialog.setCancelable(true);
                dialog.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        mImageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getReportText());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(shareIntent);
            }
        });
    }

    private String getReportText() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = simpleDateFormat.format(mTask.getDate());
        String report = getString(R.string.task_report,
                mTask.getTitle(),
                mTask.getComment(),
                dateString,
                mTask.getState().toString());
        return report;
    }

    private void grantTemPermissionForTakePicture(Intent takePictureIntent, Uri photoURI) {
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                takePictureIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity : activities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
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
        } else if (requestCode == TIME_PICKER_REQUEST_CODE) {
            Date userSelectedDate = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_DATE);
            Calendar userSelectedCalender = Calendar.getInstance();
            userSelectedCalender.setTime(userSelectedDate);
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), userSelectedCalender.get(Calendar.HOUR), userSelectedCalender.get(Calendar.MINUTE));
            mUpdateTask.setDate(mCalendar.getTime());
            updateUI();
        } else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            updatePhoto();

            Uri photoUri = FileProvider.getUriForFile(
                    getActivity(),
                    FILE_PROVIDER_AUTHORITY,
                    mPhotoFile);
            getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private void updatePhoto() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageViewShowImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_image_available));
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageViewShowImage.setImageBitmap(bitmap);
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
        mButtonCapture = view.findViewById(R.id.capture_button);
        mImageViewShowImage = view.findViewById(R.id.imageView_task_detail);
        mImageButtonShare = view.findViewById(R.id.share_image_button);
    }
}