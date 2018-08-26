package com.zn.google_android_dev_exam_practice.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;

public class AsyncTaskJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        long delay = Integer.parseInt(sp.getString("delay_notifications", "0")) * 1000L;

        new AsyncTask<Long, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                NotificationHelper.showNotification(AsyncTaskJobService.this);
                jobFinished(jobParameters, false);
            }

            @Override
            protected Void doInBackground(Long... longs) {
                try {
                    Thread.sleep(longs[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(delay);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
