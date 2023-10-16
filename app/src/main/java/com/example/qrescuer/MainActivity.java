package com.example.qrescuer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnScan;
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout); // Initialize the button view
        textView = findViewById(R.id.user_details); // Initialize the textView view
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

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
                String buildingId = result.getContents(); // Assuming the QR code contains the Firestore document ID

                // Query Firestore using the Firestore document ID
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference buildingRef = db.collection("buildings").document(buildingId);

                buildingRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve building information
                            String address = document.getString("Address");
                            int numResidents = Objects.requireNonNull(document.getLong("numResidents")).intValue();
                            String fireExits = document.getString("fireExits");
                            String disabledResidents = document.getString("disabledResidents");

                            // Start the 'Information' activity and pass the data as extras
                            Intent intent = new Intent(MainActivity.this, Information.class);
                            intent.putExtra("buildingId", buildingId);
                            intent.putExtra("address", address);
                            intent.putExtra("numResidents", numResidents);
                            intent.putExtra("fireExits", fireExits);
                            intent.putExtra("disabledResidents", disabledResidents);
                            startActivity(intent);
                        } else {
                            // Building document does not exist
                            showErrorMessage("Building not found");
                        }
                    } else {
                        // Handle other errors
                        showErrorMessage("Error fetching building data");
                    }
                });
            }
        }

    }
    // Define a method to show an error message
    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

}