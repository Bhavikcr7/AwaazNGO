package com.decimalcorp.aditya.awaazngo;

import android.app.Fragment;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

public class EventsActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_dehaze_black_24dp));
        }
        setActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Fragment fragment = new BottomFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.bottom_fragment,fragment)
                .commit();
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
        }
        return false;
    }

}
