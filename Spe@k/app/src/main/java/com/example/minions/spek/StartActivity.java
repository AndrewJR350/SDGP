package com.example.minions.spek;


import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class StartActivity extends AppCompatActivity {
    private final String TAG = "DeviceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Thread thread = new Thread("thread01");
        thread.start();



        Log.i(TAG, "Check 01");
        try {
            thread.sleep(3000);
            Intent intent = new Intent(StartActivity.this, SelectionActivity.class);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
