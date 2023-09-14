package com.example.qrescuer;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;

public class MyCaptureActivity extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}