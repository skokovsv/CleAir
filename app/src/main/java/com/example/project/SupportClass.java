package com.example.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.security.Provider;

public class SupportClass extends Service {

    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "Cat channel";
    private NotificationManager nm;

    @Override
    public void onCreate() {
        //super.onCreate();
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

        @Nullable
        @Override
        public IBinder onBind (Intent intent){
            return null;
        }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotif() {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent  pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_pets_black_24)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setContentTitle("Держу в курсе")
                .setContentText("индика подруга")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createChannelIfNeeded(nm);
        nm.notify(NOTIFY_ID,builder.build());


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createChannelIfNeeded(NotificationManager manager){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(notificationChannel);
    }


}