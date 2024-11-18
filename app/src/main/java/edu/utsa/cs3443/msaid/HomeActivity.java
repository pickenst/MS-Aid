package edu.utsa.cs3443.msaid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.utsa.cs3443.msaid.model.Alarm;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<Alarm> alarms;
    private AlarmList alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent alarmLink = new Intent(this, AlarmActivity.class);

        Button alarmsButton = findViewById(R.id.alarms_button);
        Button medicationButton = findViewById(R.id.medication_button);
        Button profileButton = findViewById(R.id.profile_button);

        alarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Alarms", Toast.LENGTH_LONG).show();
                startActivity(alarmLink);
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

        RecyclerView alarmRecyclerView = findViewById(R.id.alarmRecyclerView);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        alarmList = new AlarmList(alarms);
        alarmRecyclerView.setAdapter(alarmList);
    }

    private void loadAlarmsFromCsv() {
        alarms.clear();
        try (FileInputStream fis = openFileInput("alarms.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    alarms.add(new Alarm(parts[0], parts[1], parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load alarms from CSV", Toast.LENGTH_LONG).show();
        }
    }
}
