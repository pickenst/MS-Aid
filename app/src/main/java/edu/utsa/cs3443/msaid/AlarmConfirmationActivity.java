package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.utsa.cs3443.msaid.model.Alarm;

public class AlarmConfirmationActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";
    private String alarmName;
    private String alarmTime;
    private String alarmAmPm;
    private ArrayList<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_confirmation);
        Intent yesLink = new Intent(this, AlarmActivity.class);
        Intent noLink = new Intent(this, AlarmDeleteActivity.class);

        alarmName = getIntent().getStringExtra("alarm_name");
        alarmTime = getIntent().getStringExtra("alarm_time");
        alarmAmPm = getIntent().getStringExtra("alarm_am_pm");
        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        Button yesButton = findViewById(R.id.yes_button);
        Button noButton = findViewById(R.id.no_button);
        Button backButton = findViewById(R.id.back_button);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmName != null && alarmTime != null && alarmAmPm != null) {
                    deleteAlarm(alarmName, alarmTime, alarmAmPm);
                    Toast.makeText(view.getContext(), "Alarm Deleted", Toast.LENGTH_LONG).show();
                    startActivity(yesLink);
                } else {
                    Toast.makeText(view.getContext(), "Failed to delete alarm", Toast.LENGTH_LONG).show();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Delete Cancelled", Toast.LENGTH_LONG).show();
                startActivity(noLink);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving back to Delete Alarms", Toast.LENGTH_LONG).show();
                startActivity(noLink);
            }
        });
    }

    private void loadAlarmsFromCsv() {
        try (FileInputStream fis = openFileInput(ALARM_FILE);
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

    private void deleteAlarm(String name, String time, String amPm) {
        ArrayList<Alarm> updatedAlarms = new ArrayList<>();
        for (Alarm alarm : alarms) {
            if (!(alarm.getName().equals(name) && alarm.getTime().equals(time) && alarm.getAmPm().equals(amPm))) {
                updatedAlarms.add(alarm);
            }
        }

        try (FileOutputStream fos = openFileOutput(ALARM_FILE, MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (Alarm alarm : updatedAlarms) {
                String alarmData = alarm.getName() + "," + alarm.getTime() + "," + alarm.getAmPm() + "\n";
                writer.write(alarmData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to delete alarm", Toast.LENGTH_LONG).show();
        }
    }
}
