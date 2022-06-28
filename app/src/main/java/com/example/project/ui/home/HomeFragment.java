package com.example.project.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Worker;

import com.example.project.CustomArrayAdapter;
import com.example.project.Dat1;
import com.example.project.ForeService;
import com.example.project.ListItemClass;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.SupportClass;
import com.example.project.databinding.FragmentHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private Document doc;
    private Document doc1;
    private Thread secThread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private DatabaseReference mDataBase;
    private String TEMP_KEY="Temp";
    private String VLAG_KEY="VLAG";
    private String GAS_KEY="GAS";
    private String SMOKE_KEY="SMOKE";

    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "Cat channel";
    private NotificationManager notificationManager;

    public static int znak=0;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.refreshLayout);


        listView = (ListView)root.findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(getActivity(), R.layout.listitem1, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);
//        Intent serviceIntent = new Intent(getContext(), ForeService.class);
//        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
//        ContextCompat.startForegroundService(getContext(), serviceIntent);
        init();



        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox
                        init();

                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        Timer myTimer;
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                g();
            }
        }, 0, 600000);

        return root;
    }



    private void init() {


        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();

    }


    private void getWeb() {
        try {
            doc = Jsoup.connect("https://www.banki.ru/products/currency/cash/kursk/").get();
            doc1=Jsoup.connect("https://time100.ru/").get();
            Elements t=doc1.getElementsByTag("h3");
            Element t1=t.get(0);
            Elements tables = doc.getElementsByTag("tbody");
            Element table = tables.get(0);
            Elements elements_from_table = table.children();
            Log.d("MyLog", "size: " + table.children().get(1).child(6).text());

            ListItemClass items = new ListItemClass();
            items.setData_1(table.children().get(1).child(1).text());
            items.setData_2(table.children().get(2).child(1).text());
            items.setData_3(table.children().get(2).child(2).text());
            items.setData_4(table.children().get(2).child(4).text());
            //items.setData_5(table.children().get(4).child(1).text());
            items.setData_5(t1.text());
            arrayList.clear();

            arrayList.add(items);

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                    mDataBase = FirebaseDatabase.getInstance().getReference(TEMP_KEY);
                    String id=mDataBase.getKey();
                    String name="Температура";
                    String value=items.getData_1().toString();
                    Date time = Calendar.getInstance().getTime();
                    Dat1 new_temp=new Dat1(id,name,value,time.toString());
                    mDataBase.push().setValue(new_temp);


                    mDataBase = FirebaseDatabase.getInstance().getReference(VLAG_KEY);
                    String id1=mDataBase.getKey();
                    String name1="Влажность";
                    String value1=items.getData_2().toString();
                    Date time1 = Calendar.getInstance().getTime();
                    Dat1 new_temp1=new Dat1(id1,name1,value1,time1.toString());
                    mDataBase.push().setValue(new_temp1);

                    mDataBase = FirebaseDatabase.getInstance().getReference(GAS_KEY);
                    String id2=mDataBase.getKey();
                    String name2="Природный газ";
                    String value2=items.getData_3().toString();
                    Date time2 = Calendar.getInstance().getTime();
                    Dat1 new_temp2=new Dat1(id2,name2,value2,time2.toString());
                    mDataBase.push().setValue(new_temp2);

                    mDataBase = FirebaseDatabase.getInstance().getReference(SMOKE_KEY);
                    String id3=mDataBase.getKey();
                    String name3="Дым";
                    String value3=items.getData_4().toString();
                    Date time3 = Calendar.getInstance().getTime();
                    Dat1 new_temp3=new Dat1(id3,name3,value3,time3.toString());
                    mDataBase.push().setValue(new_temp3);




    
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
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
        items.setData_2(table.children().get(2).child(1).text());
        items.setData_3(table.children().get(1).child(1).text());
        items.setData_4(table.children().get(2).child(4).text());
        //items.setData_5(table.children().get(4).child(1).text());
        items.setData_5(t1.text());
        arrayList.clear();

        arrayList.add(items);

        mDataBase = FirebaseDatabase.getInstance().getReference(TEMP_KEY);
        String id=mDataBase.getKey();
        String name="Температура";
        String value=items.getData_1().toString();
        Date time = Calendar.getInstance().getTime();
        Dat1 new_temp=new Dat1(id,name,value,time.toString());
        mDataBase.push().setValue(new_temp);

        mDataBase = FirebaseDatabase.getInstance().getReference(VLAG_KEY);
        String id1=mDataBase.getKey();
        String name1="Влажность";
        String value1=items.getData_2().toString();
        Date time1 = Calendar.getInstance().getTime();
        Dat1 new_temp1=new Dat1(id1,name1,value1,time1.toString());
        mDataBase.push().setValue(new_temp1);

        mDataBase = FirebaseDatabase.getInstance().getReference(GAS_KEY);
        String id2=mDataBase.getKey();
        String name2="Природный газ";
        String value2=items.getData_3().toString();
        Date time2 = Calendar.getInstance().getTime();
        Dat1 new_temp2=new Dat1(id2,name2,value2,time2.toString());
        mDataBase.push().setValue(new_temp2);

        mDataBase = FirebaseDatabase.getInstance().getReference(SMOKE_KEY);
        String id3=mDataBase.getKey();
        String name3="Дым";
        String value3=items.getData_4().toString();
        Date time3 = Calendar.getInstance().getTime();
        Dat1 new_temp3=new Dat1(id3,name3,value3,time3.toString());
        mDataBase.push().setValue(new_temp3);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void not(String title,String text){
        if (getActivity() != null) {
            notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext() ,CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_pets_black_24)
                    .setAutoCancel(false)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            createChannelIfNeeded(notificationManager);
            notificationManager.notify(NOTIFY_ID,builder.build());
        }






   }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createChannelIfNeeded(NotificationManager manager){
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(notificationChannel);
    }
}

