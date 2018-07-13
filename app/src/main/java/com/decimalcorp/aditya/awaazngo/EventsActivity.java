package com.decimalcorp.aditya.awaazngo;

import android.app.Fragment;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EventsActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Fragment fragment = new BottomFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.bottom_fragment, fragment)
                .commit();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Events");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Event event = new Event();
                    event.name = String.valueOf(postSnapshot.child("Name").getValue());
                    event.details = String.valueOf(postSnapshot.child("Details").getValue());
                    event.add = String.valueOf(postSnapshot.child("PhotoAdd").getValue());
                    event.date = String.valueOf(postSnapshot.child("Date").getValue());

                    eventList.add(event);
                    Toast.makeText(EventsActivity.this, "longDate = " + event.getLongDate() , Toast.LENGTH_SHORT).show();


                }
                long current = Calendar.getInstance().getTimeInMillis();
                if(eventList.size()> 0) {
                    if (eventList.get(0).getLongDate() >= current)
                        Log.d("#######################", eventList.get(0).getDate() + " is Upcoming Event");
                    else
                        Log.d("#######################", eventList.get(0).getDate() + " is Past Event");
                    if (eventList.get(1).getLongDate() >= current)
                        Log.d("#######################", eventList.get(1).getDate() + " is Upcoming Event");
                    else
                        Log.d("#######################", eventList.get(1).getDate() + " is Past Event");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class Event {
        String name;
        String details;
        String date;
        String add;

        Event() {}

        Event(String n, String d, String t, String a) {
            name = n;
            details = d;
            date = t;
            add = a;
        }

        String getName(){ return name;}
        String getDetails(){ return details;}
        String getAdd(){ return add; }
        String getDate(){ return date;}
        long getLongDate(){
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date lDate = simpleDateFormat.parse(date);
                return lDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
