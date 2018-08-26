package com.zn.google_android_dev_exam_practice.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // Do your job action here. Anything done here must be done on the main thread
        NotificationHelper.showNotification(this);

        // Return true if there is still work that needs to be done on a separate thread. Otherwise, false
        // indicates no more work needs to be done
        // The other thread will call jobFinished() once it's done, rendering the job complete
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // Return true to let system reschedule job. Otherwise, false indicates drop the job
        return true;
    }
}
