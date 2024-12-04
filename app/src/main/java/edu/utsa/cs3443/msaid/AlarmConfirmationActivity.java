package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.util.Log;
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

/**
 * AlarmConfirmationActivity provides the confirmation screen for deleting an alarm.
 * Users can confirm the deletion or go back to the previous activity.
 * It handles loading alarms from the alarm CSV and deleting a specific alarm.
 */
public class AlarmConfirmationActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";
    private static final String TAG = "AlarmConfirmationActivity"; // Defines a tag for logging
    private String alarmName;
    private String alarmTime;
    private String alarmAmPm;
    private ArrayList<Alarm> alarms; // Lists all the alarms loaded from the CSV

    /**
     * Called when the activity is created. Initializes the UI, loads the list of alarms,
     * and sets click listeners for the confirmation buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it was most recently given.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_confirmation);

        Intent yesLink = new Intent(this, AlarmActivity.class);
        Intent noLink = new Intent(this, AlarmDeleteActivity.class);

        // Retrieves the alarm information from the intent
        alarmName = getIntent().getStringExtra("alarm_name");
        alarmTime = getIntent().getStringExtra("alarm_time");
        alarmAmPm = getIntent().getStringExtra("alarm_am_pm");

        // Initializes the list of alarms and loads them from the alarm CSV
        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        Button yesButton = findViewById(R.id.yes_button);
        Button noButton = findViewById(R.id.no_button);
        Button backButton = findViewById(R.id.back_button);

        // Sets listener for the "Yes" button to confirm and delete the alarm
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if alarm info is valid
                if (alarmName != null && alarmTime != null && alarmAmPm != null) {
                    // Deletes the specified alarm
                    deleteAlarm(alarmName, alarmTime, alarmAmPm);
                    // Shows a toast message confirming the alarm has been deleted
                    Toast.makeText(view.getContext(), "Alarm Deleted", Toast.LENGTH_LONG).show();
                    // Goes back to AlarmActivity
                    startActivity(yesLink);
                } else {
                    // Shows a toast message if deletion failed
                    Toast.makeText(view.getContext(), "Failed to delete alarm", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Sets listener for the "No" button to go back to AlarmDeleteActivity
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Goes back to AlarmDeleteActivity
                startActivity(noLink);
            }
        });

        // Sets listener for the "Back" button to go back to AlarmDeleteActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Goes back to AlarmDeleteActivity
                startActivity(noLink);
            }
        });
    }

    /**
     * Loads all the alarms from the CSV and populates the alarm list.
     * Displays a toast message if loading the list fails.
     */
    private void loadAlarmsFromCsv() {
        try (FileInputStream fis = openFileInput(ALARM_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            // Reads each line from the Alarm CSV and parses the alarm info
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    // Creates a new Alarm object and adds it to the list
                    alarms.add(new Alarm(parts[0], parts[1], parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            // Logs an error message if loading fails
            Log.e(TAG, "Failed to load alarms from CSV", e);
            // Shows a toast message showing that it failed to load alarms
            Toast.makeText(this, "Failed to load alarms from CSV", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Deletes the alarm from the list and then updates the CSV to reflect the changes.
     * Displays a toast message if deleting the alarm fails.
     *
     * @param name The name of the alarm that will be deleted.
     * @param time The time of the alarm that will be deleted.
     * @param amPm The AM/PM of the alarm that will be deleted.
     */
    private void deleteAlarm(String name, String time, String amPm) {
        ArrayList<Alarm> updatedAlarms = new ArrayList<>();
        // Goes through the list of alarms and then picks out the alarm to be deleted
        for (Alarm alarm : alarms) {
            if (!(alarm.getName().equals(name) && alarm.getTime().equals(time) && alarm.getAmPm().equals(amPm))) {
                updatedAlarms.add(alarm); // Adds all other alarms to the updated list
            }
        }

        // Writes the newly updated list back into the Alarm CSV
        try (FileOutputStream fos = openFileOutput(ALARM_FILE, MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            for (Alarm alarm : updatedAlarms) {
                String alarmData = alarm.getName() + "," + alarm.getTime() + "," + alarm.getAmPm() + "\n";
                writer.write(alarmData);
            }
        } catch (IOException e) {
            // Logs an error message in case the deletion fails
            Log.e(TAG, "Failed to delete alarm", e);
            // Shows a toast message indicating that it failed to delete the alarm
            Toast.makeText(this, "Failed to delete alarm", Toast.LENGTH_LONG).show();
        }
    }
}