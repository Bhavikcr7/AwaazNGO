package com.decimalcorp.aditya.awaazngo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity{

    Toolbar toolbar;
    ViewPager viewPager;
    MyViewPagerAdapter viewPagerAdapter;
    int[] layouts;
    TabLayout indicator;

    private void haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }

        if(haveConnectedMobile == false)
            if(haveConnectedWifi == false)
                Toast.makeText(MainActivity.this,"NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        Log.d("TAG","Mobile Connection state = "+ haveConnectedMobile);
        Log.d("TAG","Wifi Connection state = "+ haveConnectedWifi);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_dehaze_black_24dp));
        }
        setActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3};
        viewPagerAdapter = new MyViewPagerAdapter(this,layouts);
        viewPager.setAdapter(viewPagerAdapter);
        indicator.setupWithViewPager(viewPager);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        Fragment fragment = null;
        FragmentManager fragmentManager = this.getFragmentManager();
        fragment = new ButtonsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.button_fragment,fragment)
                .commit();
        Fragment fragment1 = new BottomFragment();
        fragmentManager.beginTransaction()
                .add(R.id.bottom_fragment, fragment1)
                .commit();

    }


    public class MyViewPagerAdapter extends PagerAdapter {

        private Context context;
        int[] layouts;

        public MyViewPagerAdapter(Context context, int[] l) {
            this.context = context;
            this.layouts = l;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layouts[position], null);
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager viewPager = (ViewPager) container;
            View view = (View) object;
            viewPager.removeView(view);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                break;
            case R.id.menu_who_we_are:
                break;
            case R.id.menu_events:
                startActivity(new Intent(MainActivity.this,EventsActivity.class));
                break;
        }
        return false;
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < layouts.length - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        haveNetworkConnection();
                    } else {
                        viewPager.setCurrentItem(0);
                        haveNetworkConnection();
                    }
                }
            });
        }
    }
}
