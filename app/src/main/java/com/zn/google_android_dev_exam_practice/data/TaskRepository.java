package com.zn.google_android_dev_exam_practice.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Repositories abstract access to multiple data sources, if your app has any. It is a convenience
 * class that handles data operations and liaising updates between the Dao and a Network. Access
 * your database through the repository object, rather than directly, so if you have multiple data
 * sources, this single repository can handle interacting with all of them for you. That being said,
 * this seems like a "Content Provider", doesn't it?
 * <p>
 * It is common to assess whether to update data and fetch fresh data, or to provide cached data
 */
public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.getAllTasks(); // retrieves cached data
    }

    LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        new InsertAsyncTask(mTaskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateAsyncTask(mTaskDao).execute(task);
    }

    public void delete(long id) {
        new DeleteAsyncTask(mTaskDao).execute(id);
    }

    /**
     * Use an AsyncTask to properly insert a new task into the database
     */
    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        InsertAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.insert(tasks[0]);
            return null;
        }
    }

    /**
     * Use an AsyncTask to properly update an existing task in the database
     */
    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        UpdateAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.update(tasks[0]);
            return null;
        }
    }

    /**
     * Use an AsyncTask to properly update an existing task in the database
     */
    private static class DeleteAsyncTask extends AsyncTask<Long, Void, Void> {
        private TaskDao mAsyncTaskDao;

        DeleteAsyncTask(TaskDao taskDao) {
            mAsyncTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Long... ids) {
            mAsyncTaskDao.delete(ids[0]);
            return null;
        }
    }

}
