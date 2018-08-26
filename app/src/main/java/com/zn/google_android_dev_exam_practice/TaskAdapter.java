package com.zn.google_android_dev_exam_practice;

import android.arch.paging.PagedListAdapter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zn.google_android_dev_exam_practice.data.Task;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The RecyclerView adapter is nothing special when integrating with Room. The only thing to note
 * is that the LiveData observer is what calls {@code setTasksList()}. Binding views is all the same
 *
 * When using the paging library with RecyclerView, use PagedListAdapter. This extends the
 * RecyclerViewAdapter and RecyclerViewViewHolder
 */
public class TaskAdapter extends PagedListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private ClickListener mListener;

    interface ClickListener {
        void onClick(Task task);
    }

    private static DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            // Although task details are changed if reloaded from database, id is the same
            return oldTask.getId() == newTask.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.equals(newTask);
        }
    };

    TaskAdapter(ClickListener mListener) {
        super(DIFF_CALLBACK);
        this.mListener = mListener;
    }

    public Task getTaskAtPosition(int position) {
        if (getItemCount() > 0) {
            return getItem(position);
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
        Task task = getItem(i);
        if (task != null)
            taskViewHolder.bindView(task);
        else {
            // If null, then item is a placeholder, so once the actual object is loaded,
            // PagedListAdapter automatically invalidates this row
            taskViewHolder.clear();
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

        void bindView(Task task) {
            mTaskName.setText(task.getTaskName());
            long date = task.getDueDate();
            String dateString = DateFormat.getDateInstance().format(new Date(date));
            mTaskDate.setText(itemView.getContext()
                    .getString(R.string.due_date, dateString));
            mTaskStatus.setColorFilter(!task.isCompleted() ? Color.RED : Color.GREEN);
        }

        void clear() {
            mTaskName.setText("");
            mTaskDate.setText("");
            mTaskStatus.setColorFilter(Color.TRANSPARENT);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getItem(getAdapterPosition()));
        }
    }
}
