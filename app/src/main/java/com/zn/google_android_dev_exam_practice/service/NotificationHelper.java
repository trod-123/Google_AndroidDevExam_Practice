package com.zn.google_android_dev_exam_practice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zn.google_android_dev_exam_practice.R;
import com.zn.google_android_dev_exam_practice.TaskListActivity;

public class NotificationHelper {

    public static void showNotification(Context context) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Tasks reminder!")
                .setContentText("You have incomplete tasks due soon")
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channel_01", "For the droids", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(notificationChannel);
            builder.setChannelId("channel_01");
        }
        nm.notify(12, builder.build());
    }
}
