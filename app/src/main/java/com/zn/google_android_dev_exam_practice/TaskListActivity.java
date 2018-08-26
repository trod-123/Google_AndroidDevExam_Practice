package com.zn.google_android_dev_exam_practice;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.zn.google_android_dev_exam_practice.data.Task;
import com.zn.google_android_dev_exam_practice.data.TaskViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListActivity extends AppCompatActivity {

    @BindView(R.id.rv_tasklist)
    RecyclerView mRecyclerTaskView;
    TaskAdapter mTaskAdapter;

    private TaskViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);

        // Prepare the RecyclerView
        mRecyclerTaskView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        mRecyclerTaskView.setHasFixedSize(false);
        mTaskAdapter = new TaskAdapter(new TaskAdapter.ClickListener() {
            @Override
            public void onClick(Task task) {
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class)
                        .putExtra("taskName", task.getTaskName())
                        .putExtra("taskDeadline", task.getDueDate())
                        .putExtra("taskId", task.getId());
                startActivityForResult(intent, 2);
            }
        });
        mRecyclerTaskView.setAdapter(mTaskAdapter);

        // Enable swipe gestures for RecyclerView
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                Task task = mTaskAdapter.getTaskAtPosition(position);
                mTaskViewModel.delete(task.getId());
            }
        });
        helper.attachToRecyclerView(mRecyclerTaskView);

        // This is the proper way of getting view models. You do NOT instantiate it here.
        // As its name indicated, ViewModelProviders is what manages your app's view models. The
        // view models are created when the app first starts, so all you need to do is grab it
        // from the ViewModelProviders. By having the ViewModelProviders "own" the view models
        // helps allow view models to live outside the activity, fragment, or view it's hosted in
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mTaskViewModel.getAllTasks().observe(this, new Observer<PagedList<Task>>() {
            @Override
            public void onChanged(@Nullable PagedList<Task> tasks) {
                mTaskAdapter.submitList(tasks);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Task task = new Task(data.getStringExtra("taskName"), data.getLongExtra("taskDeadline", 0L));
                mTaskViewModel.insert(task);
                Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getAction() != null && data.getAction().equals("delete")) {
                    mTaskViewModel.delete(data.getLongExtra("taskId", -1));
                } else {
                    Task task = new Task(data.getStringExtra("taskName"), data.getLongExtra("taskDeadline", 0L));
                    task.setId(data.getLongExtra("taskId", -1));
                    mTaskViewModel.update(task);
                    Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void launchAddNewTaskActivity(View view) {
        startActivityForResult(new Intent(
                TaskListActivity.this, AddTaskActivity.class), 1);
    }
}
