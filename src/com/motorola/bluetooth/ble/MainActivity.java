package com.motorola.bluetooth.ble;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

	private TabHost tabHost; 
	private Intent intent=new Intent();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tabHost = getTabHost();
        TabHost.TabSpec spec;
        
        //The 1th tab: personal Info
        intent = new Intent().setClass(this, PersonalActivity.class);
        spec = tabHost.newTabSpec("personal info").setIndicator("", this.getResources().getDrawable(R.drawable.ic_tab_personal)).setContent(intent);
        tabHost.addTab(spec);
        
        //The 2nd tab: sensor data
        intent = new Intent().setClass(this, Motorola_sample_apk_icsActivity.class);
        spec = tabHost.newTabSpec("sensor data").setIndicator("", this.getResources().getDrawable(R.drawable.ic_tab_seosor)).setContent(intent);
        tabHost.addTab(spec);
        //The 3rd tab: feedback
        intent = new Intent().setClass(this, FeedbackActivity.class);
        spec = tabHost.newTabSpec("feedback").setIndicator("", this.getResources().getDrawable(R.drawable.ic_tab_feedback)).setContent(intent);
        tabHost.addTab(spec);
        
        //The 4th tab: config
        intent = new Intent().setClass(this, DiagnosisActivity.class);
        spec = tabHost.newTabSpec("setting").setIndicator("", this.getResources().getDrawable(R.drawable.ic_tab_setting)).setContent(intent);
        tabHost.addTab(spec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
