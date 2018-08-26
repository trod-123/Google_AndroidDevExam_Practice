package com.zn.google_android_dev_exam_practice.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

public class AsyncTaskJobService extends JobService {

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
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
        }.execute(5000L);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
