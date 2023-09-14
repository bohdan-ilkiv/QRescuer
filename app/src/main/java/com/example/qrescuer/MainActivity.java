package com.example.qrescuer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(v -> ScanCode());
    }

    private void ScanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Кнопка збільшення звуку - включити ліхтар");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.initiateScan(); // Start the scanning activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String contents = result.getContents();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Result");
                builder.setMessage(contents);
                builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
            }
        }
    }
}