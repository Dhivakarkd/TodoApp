package com.example.recyclerdemo.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.recyclerdemo.R;

public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_NAME ="Todo_AlarmChannel";
    public static final String CHANNEL_ID ="Todo_AlarmChannel_1";
    private NotificationManager mNotificationManager;
    Context help;
    public NotificationHelper(Context base) {
        super(base);
        help=base;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel todo_channel =new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                todo_channel.enableLights(true);
                todo_channel.setLightColor(android.R.color.holo_green_dark);
                todo_channel.setLockscreenVisibility(NotificationManager.IMPORTANCE_MAX);

                getNofiticationManager().createNotificationChannel(todo_channel);
    }

    public NotificationManager getNofiticationManager(){
        if (mNotificationManager ==null){
            mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    public NotificationCompat.Builder getNotification(String Title, String Message){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle(Title)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(Message)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher))
                    .setAutoCancel(true);

      }
        else{
            return new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(Title)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText(Message)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher))
                    .setAutoCancel(true);

        }
    }
}
