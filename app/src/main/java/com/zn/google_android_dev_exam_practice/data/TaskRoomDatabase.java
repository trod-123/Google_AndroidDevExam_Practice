package com.zn.google_android_dev_exam_practice.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static TaskRoomDatabase INSTANCE;

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context, TaskRoomDatabase.class,
                            "task_database")
                            // Wipes and rebuilds instead of migrating if no migration object is
                            // available
                            .fallbackToDestructiveMigration()
                            // Callback for doing things as the database is built. This is not
                            // necessary to include, but it is here to give us initial dummy
                            // data to work with... although this is where we could initialize
                            // the data...
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Adding a callback during the INSTANCE build() process that populates the database
     *
     * Preferable to call onOpen to perform database inits and refreshes than in onCreate since
     * onCreate is only called once
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Loads dummy data
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private TaskRoomDatabase db;

        PopulateDbAsync(TaskRoomDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (db.taskDao().getAnyTask().length == 0) {
                // Initialize data if database is null
                loadDummyData();
            }

            return null;
        }

        /**
         * Needs to be called from a worker thread
         */
        private void loadDummyData() {
            Task task1 = new Task("First task for the day!",
                    System.currentTimeMillis() - 100000L);
            Task task2 = new Task("Second task for the day!",
                    System.currentTimeMillis() + 1000L);
            Task task3 = new Task("Third task... for far away in the future!",
                    System.currentTimeMillis() + 100000L);
            Task task4 = new Task("And another task... this one is old",
                    System.currentTimeMillis() - 123456L);
            Task task5 = new Task("And this task is due now!",
                    System.currentTimeMillis());

            db.taskDao().insert(task1);
            db.taskDao().insert(task2);
            db.taskDao().insert(task3);
            db.taskDao().insert(task4);
            db.taskDao().insert(task5);
        }
    }
}
