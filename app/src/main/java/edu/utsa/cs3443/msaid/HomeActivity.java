package edu.utsa.cs3443.msaid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button alarmsButton = findViewById(R.id.alarms_button);
        Button medicationButton = findViewById(R.id.medication_button);
        Button profileButton = findViewById(R.id.profile_button);
        alarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Alarms Button Pressed", Toast.LENGTH_LONG).show();
            }
        });
        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Medication Button Pressed", Toast.LENGTH_LONG).show();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Profile Button Pressed", Toast.LENGTH_LONG).show();
            }
        });

    }
}