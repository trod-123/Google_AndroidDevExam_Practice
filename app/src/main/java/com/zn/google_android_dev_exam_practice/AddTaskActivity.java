package com.zn.google_android_dev_exam_practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.et_taskName)
    EditText mTaskName;
    @BindView(R.id.cv_taskDeadline)
    CalendarView mTaskDeadline;
    @BindView(R.id.btn_addNewTask)
    Button mAddTask;
    @BindView(R.id.btn_deleteTask)
    Button mDeleteTask;

    boolean update = false;
    long id;
    long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskName")) {
            mTaskName.setText(intent.getStringExtra("taskName"));
            mTaskDeadline.setDate(intent.getLongExtra("taskDeadline", 0L));
            getSupportActionBar().setTitle("Edit task details");
            mAddTask.setText("Update task!");
            mAddTask.setEnabled(true);
            id = intent.getLongExtra("taskId", 0);
            mDeleteTask.setVisibility(View.VISIBLE);
            update = true;
        }

        selectedDate = mTaskDeadline.getDate();

        mTaskDeadline.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);
                selectedDate = c.getTimeInMillis();
                Toast.makeText(AddTaskActivity.this, DateFormat.getDateInstance().format(selectedDate), Toast.LENGTH_SHORT).show();
            }
        });

        // Enable the button only if there is a task name set
        mTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) mAddTask.setEnabled(true);
                else mAddTask.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTaskName.requestFocus();
    }

    public void addNewTask(View view) {
        // Bundle the task info in an intent that is sent back to the main activity that has the
        // ViewModel
        Intent intent = new Intent()
                .putExtra("taskName", mTaskName.getText().toString())
                .putExtra("taskDeadline", selectedDate);
        if (update) intent.putExtra("taskId", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void deleteTask(View view) {
        Intent intent = new Intent()
                .setAction("delete")
                .putExtra("taskId", id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
