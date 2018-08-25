package com.zn.google_android_dev_exam_practice.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * An entity is a pojo with room annotations. Make sure the _id also has its setters and getters
 */
@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    @NonNull
    @ColumnInfo(name = "taskName")
    private String mTaskName;

    @ColumnInfo(name = "isComplete")
    private boolean mCompleted;

    @ColumnInfo(name = "dueDate")
    private long mDueDate;

    public Task(@NonNull String taskName, long dueDate) {
        mTaskName = taskName;
        mDueDate = dueDate;
        mCompleted = false;
    }

    /**
     * Creates a task with a specified id
     * <p>
     * Because Room only expects one constructor per Entity class, annotate with {@code @Ignore}
     * since we only want to use this when editing tasks
     *
     * @param id
     * @param taskName
     * @param dueDate
     * @param completed
     */
    @Ignore
    public Task(long id, @NonNull String taskName, long dueDate, boolean completed) {
        _id = id;
        mTaskName = taskName;
        mDueDate = dueDate;
        mCompleted = completed;
    }

    public void setId(long id) {
        _id = id;
    }

    public long getId() {
        return _id;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public long getDueDate() {
        return mDueDate;
    }

    public void setDueDate(long dueDate) {
        mDueDate = dueDate;
    }
}
