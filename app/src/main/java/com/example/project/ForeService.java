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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.project.ui.home.HomeFragment;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ForeService extends Service {


    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int NOTIFY_ID = 101;
    private NotificationManager notificationManager;


    private Document doc;
    private Document doc1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = new Bundle();
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);//!!!!!!!!
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Анализатор")
                .setContentText(input)
                .setSmallIcon(R.drawable.baseline_pets_black_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        Timer myTimer;
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                g();
            }
        }, 0, 6000);








        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void g() {
        try {
            doc = Jsoup.connect("https://www.banki.ru/products/currency/cash/kursk/").get();
            doc1=Jsoup.connect("https://time100.ru/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements t=doc1.getElementsByTag("h3");
        Element t1=t.get(0);
        Elements tables = doc.getElementsByTag("tbody");
        Element table = tables.get(0);
        Elements elements_from_table = table.children();
        Log.d("MyLog", "size: " + table.children().get(1).child(6).text());

        ListItemClass items = new ListItemClass();
        items.setData_1(table.children().get(1).child(1).text());
        String strt=table.children().get(1).child(1).text().substring(0,2);

        items.setData_2(table.children().get(2).child(1).text());
        String strv=table.children().get(2).child(1).text().substring(0,2);

        items.setData_3(table.children().get(1).child(1).text());
        String strs=table.children().get(1).child(1).text().substring(0,2);

        items.setData_4(table.children().get(2).child(4).text());
        String strg=table.children().get(2).child(4).text().substring(0,2);
        //items.setData_5(table.children().get(4).child(1).text());
        items.setData_5(t1.text());





        int temp=Integer.parseInt(strt);
        int vlag = Integer.parseInt(strv);
        int smoke = Integer.parseInt(strs);
        int gas=Integer.parseInt(strg);

        if(temp>54 || temp<18){
            f("Температура","Температура вышла за допустимое значениие",2);
        }
        if(vlag>80){
            f("Влажность","Влажность вышла за допустимое значениие",3);
        }
        if(smoke>10){
            f("Дым","Дым вышел за допутимое значение",4);
        }
        if(gas>75){
            f("Природный газ","Обнаружено повышение природного газа",5);
        }


    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public void f(String title,String text,int id){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this ,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(false)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        createChannelIfNeeded(notificationManager);
        notificationManager.notify(id,builder.build());
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createChannelIfNeeded(NotificationManager manager){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(notificationChannel);
    }

}


