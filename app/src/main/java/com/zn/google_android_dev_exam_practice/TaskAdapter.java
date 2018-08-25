package com.zn.google_android_dev_exam_practice;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zn.google_android_dev_exam_practice.data.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The RecyclerView adapter is nothing special when integrating with Room. The only thing to note
 * is that the LiveData observer is what calls {@code setTasksList()}. Binding views is all the same
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    ClickListener mListener;
    private List<Task> mTasks; // caches the tasks list in the adapter

    interface ClickListener {
        void onClick(Task task);
    }

    public TaskAdapter(ClickListener listener) {
        mListener = listener;
    }

    public void setTasksList(List<Task> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAtPosition(int position) {
        if (mTasks != null) {
            return mTasks.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task_list, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bindView(i);
    }

    @Override
    public int getItemCount() {
        if (mTasks == null) {
            return 0;
        } else {
            return mTasks.size();
        }
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_task_name)
        TextView mTaskName;
        @BindView(R.id.tv_task_date)
        TextView mTaskDate;
        @BindView(R.id.iv_task_status)
        ImageView mTaskStatus;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            Task task = mTasks.get(position);
            mTaskName.setText(task.getTaskName());
            long date = task.getDueDate();
            String dateString = DateFormat.getDateInstance().format(new Date(date));
            mTaskDate.setText(itemView.getContext()
                    .getString(R.string.due_date, dateString));
            mTaskStatus.setColorFilter(!task.isCompleted() ? Color.RED : Color.GREEN);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(mTasks.get(getAdapterPosition()));
        }
    }
}
