package com.decimalcorp.aditya.awaazngo;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EventsActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getBaseContext(),"Please wait while we update the list.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        final ListView listView1 = (ListView) findViewById(R.id.upcoming);
        final ListView listView2 = (ListView) findViewById(R.id.past);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Events");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Event event = new Event();
                    event.name = String.valueOf(postSnapshot.child("Name").getValue());
                    event.details = String.valueOf(postSnapshot.child("Details").getValue());
                    event.add = String.valueOf(postSnapshot.child("PhotoAdd").getValue());
                    event.date = String.valueOf(postSnapshot.child("Date").getValue());

                    eventList.add(event);
                }
                long current = Calendar.getInstance().getTimeInMillis();
                Collections.sort(eventList, new Sortbyroll());
                int i=0;
                for( i=0; i< eventList.size(); i++){
                    if(eventList.get(i).getLongDate() < current){
                        break;
                    }
                }
                CustomAdapter adapter = new CustomAdapter(getBaseContext(), eventList.subList(0, i+1));
                listView1.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listView1);

                CustomAdapter adapter1 = new CustomAdapter(getBaseContext(), eventList.subList(i+1, eventList.size()));
                listView2.setAdapter(adapter1);
                setListViewHeightBasedOnChildren(listView2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView1.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    class Sortbyroll implements Comparator<Event> {
        @Override
        public int compare(Event o1, Event o2) {
            long d0 = o1.getLongDate();
            long d1 = o2.getLongDate();
            int x = Long.compare(d1, d0);
            return x;
        }
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

    class CustomAdapter extends BaseAdapter {
        List<Event> mList;
        Context context;

        CustomAdapter(Context context, List<Event> mList){
            this.context = context;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            Log.d("&&&","getCount");
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("&&&","getItem");
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("&&&","getView");

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.expandable_list_item,parent, false);
            }

            TextView tv1 = (TextView) convertView.findViewById(R.id.eventDate);
            TextView tv2 = (TextView) convertView.findViewById(R.id.eventName);
            TextView tv3 = (TextView) convertView.findViewById(R.id.eventDesc);
            ImageView iv1 = (ImageView) convertView.findViewById(R.id.eventPic);
            tv1.setText(mList.get(position).getDate());
            tv2.setText(mList.get(position).getName());
            tv3.setText(mList.get(position).getDetails());
            Picasso.get().load(mList.get(position).getAdd())
                    .placeholder(R.drawable.process02)
                    .into(iv1);

            return convertView;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}