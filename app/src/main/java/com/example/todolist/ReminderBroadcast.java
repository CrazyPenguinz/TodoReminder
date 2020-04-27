package com.example.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todolist.Model.PostIt;

import java.util.List;

class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<PostIt> tomorrowList = (List<PostIt>) intent.getBundleExtra("LIST_BUNDLE").getSerializable("TOMORROW_LIST");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Tomorrow schedule");

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        for (PostIt postIt : tomorrowList) {
            SystemClock.sleep(2000);
            builder.setContentText(String.format("%s at %s.", postIt.getTitle(), postIt.getPlace()));
            notificationManagerCompat.notify(200, builder.build());
        }
    }
}
