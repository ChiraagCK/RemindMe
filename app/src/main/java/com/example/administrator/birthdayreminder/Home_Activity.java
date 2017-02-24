package com.example.administrator.birthdayreminder;

import android.app.NotificationManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Home_Activity extends AppCompatActivity  implements TabHost.OnTabChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), R.id.fragmenttabholder);
        tabHost.addTab(tabHost.newTabSpec("add_reminder").setIndicator("ADD"), AddReminder_Activity.class, null);
        tabHost.addTab(tabHost.newTabSpec("upcoming").setIndicator("UPCOMING"), Upcoming_Activity.class, null);
        tabHost.addTab(tabHost.newTabSpec("all").setIndicator("ALL"), All_Activity.class, null);


        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "KR Birthday Letters.ttf");
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(0xFFFFFFFF);
            tv.setTypeface(myTypeface);
            tv.setTextSize(20);
        }

        tabHost.setOnTabChangedListener(this);
    }
    @Override
    public void onTabChanged(String tabId) {
       // Toast.makeText(Home_Activity.this,"Tab Changed: "+tabId,Toast.LENGTH_LONG).show();
    }
}
