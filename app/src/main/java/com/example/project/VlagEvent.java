package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VlagEvent extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapterEvent adapter;
    private List<ListItemClass> listData;

    private DatabaseReference mDataBase;
    private String VLAG_KEY="VLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlag_event);
        setTitle("Влажность");
        init();
        getData();
    }

    private void init(){
        listView = findViewById(R.id.listViewEventVlag);
        listData = new ArrayList<>();
        adapter = new ArrayAdapterEvent(this, R.layout.listitem_event, listData, getLayoutInflater());
        listView.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference(VLAG_KEY);

    }

    private void getData(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(listData.size()>0) listData.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                    Dat1 dt1=ds.getValue(Dat1.class);
                    assert dt1 !=null;
                    ListItemClass items = new ListItemClass();
                    for(int i=0;i<3;i++){

                        items.setData_1(dt1.name);
                        items.setData_2(dt1.value);
                        items.setData_3(dt1.time.substring(4,16));
                    }
                    listData.add(items);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);

    }
}