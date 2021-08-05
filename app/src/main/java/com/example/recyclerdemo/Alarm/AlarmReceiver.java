package com.example.recyclerdemo.Alarm;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import static com.example.recyclerdemo.MainActivity.ALARM_INTENT_STRING_NAME;
import static com.example.recyclerdemo.MainActivity.NOTIFICAATION_TITLE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper=new NotificationHelper(context);
        String message=intent.getExtras().get(ALARM_INTENT_STRING_NAME).toString();
        NotificationCompat.Builder nb =notificationHelper.getNotification(NOTIFICAATION_TITLE,message);
        notificationHelper.getNofiticationManager().notify(1,nb.build());
    }
}
