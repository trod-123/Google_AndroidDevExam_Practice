package com.zn.google_android_dev_exam_practice.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Creating a working Dao is easy! All you need is the below and let Room take care of the rest
 *
 * It seems that we just define the methods here and RoomDatabase writes up the implementations
 */
@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("DELETE FROM task_table WHERE _id is :id")
    void delete(long id);

    @Query("SELECT * from task_table ORDER BY dueDate ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * from task_table WHERE _id is :id")
    Task getTaskById(long id);

    /**
     * Returns a random task from the database. If there are no tasks in the database, returns
     * an empty array
     * @return
     */
    @Query("SELECT * FROM task_table LIMIT 1")
    Task[] getAnyTask();
}
