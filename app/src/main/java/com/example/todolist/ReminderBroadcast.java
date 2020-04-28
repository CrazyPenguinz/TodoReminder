package com.example.todolist;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todolist.Model.PostIt;

import java.util.ArrayList;

public class ReminderBroadcast extends BroadcastReceiver {
    public static final String CHANNEL = "TOMORROW_CHANNEL";
    public static final String TOMORROW_NOTIFY_GROUP = "TOMORROW_GROUP";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            int total = bundle.size();
            if (total > 0) {
                ArrayList<PostIt> tomorrowList = bundle.getParcelableArrayList("POSTS");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentTitle("Tomorrow schedule");

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancelAll();

                for (PostIt postIt : tomorrowList) {
                    Log.d("ntc", "onReceive: notify " + postIt.getTitle());
                    SystemClock.sleep(2000);
                    builder.setContentText(String.format("%s at %s.", postIt.getTitle(), postIt.getPlaceName()));
                    builder.setGroup(TOMORROW_NOTIFY_GROUP);
                    notificationManagerCompat.notify(postIt.getId(), builder.build());
                }

                NotificationCompat.InboxStyle children = new NotificationCompat.InboxStyle()
                        .setSummaryText(tomorrowList.size() > 1?
                                String.format("%d schedules.", tomorrowList.size()) :
                                String.format("%d schedule.", tomorrowList.size()));

                Notification summaryNotification = new NotificationCompat.Builder(context, CHANNEL)
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setGroup(TOMORROW_NOTIFY_GROUP)
                        .setStyle(children)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                        .setGroupSummary(true)
                        .build();
                SystemClock.sleep(2000);
                notificationManagerCompat.notify(999, summaryNotification);
            }
        }
    }
}
