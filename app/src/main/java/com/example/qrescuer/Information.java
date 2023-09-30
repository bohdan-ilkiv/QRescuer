package com.example.qrescuer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // Retrieve building information from intent extras
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        int numResidents = intent.getIntExtra("numResidents", 0); // Default value if not found
        String fireExits = intent.getStringExtra("fireExits");
        String disabledResidents = intent.getStringExtra("disabledResidents");

        // Update the UI elements with the retrieved building information
        updateUI(address, numResidents, fireExits, disabledResidents);
    }

    // Define a method to update the UI with building information
    private void updateUI(String address, int numResidents, String fireExits, String disabledResidents) {
        // Find the TextViews or other UI elements by their IDs
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView numResidentsTextView = findViewById(R.id.numResidentsTextView);
        TextView fireExitsTextView = findViewById(R.id.fireExitsTextView);
        TextView disabledResidentsTextView = findViewById(R.id.disabledResidentsTextView);

        // Update the text of the TextViews with building information
        addressTextView.setText("Address: " + address);
        numResidentsTextView.setText("Number of Residents: " + numResidents);
        fireExitsTextView.setText("Fire Exits: " + fireExits);
        disabledResidentsTextView.setText("Disabled Residents: " + disabledResidents);
    }
}
