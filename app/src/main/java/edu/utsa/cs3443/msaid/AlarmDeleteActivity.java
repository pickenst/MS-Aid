package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.msaid.model.Alarm;
import edu.utsa.cs3443.msaid.model.User;

/**
 * AlarmDeleteActivity allows users to delete an existing alarm.
 * Users can select an alarm from the list of alarms and begin to confirm its deletion.
 * The activity also loads the list of alarms from a CSV file called alarms.csv.
 */
public class AlarmDeleteActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";
    private static final String TAG = "AlarmDeleteActivity"; // Defines a tag for the log
    private ArrayList<Alarm> alarms;
    private Alarm selectedAlarm = null; // Currently selected alarm to be deleted

    /**
     * Called when the activity is created. Sets up the UI, loads the list of alarms,
     * and sets click listeners for the buttons.
     *
     * @param savedInstanceState If the activity is being reinitialized after previously being shut down,
     * this Bundle contains the data it was most recently given.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_delete);
        User currentUser;
        Intent last = getIntent();
        currentUser = last.getParcelableExtra("user");


        Intent confirmLink = new Intent(this, AlarmConfirmationActivity.class);
        Intent alarmLink = new Intent(this, AlarmActivity.class);

        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_button);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializes the list of alarms and load them from the Alarm CSV
        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        // Sets the adapter for the RecyclerView to show the list of alarms
        AlarmList alarmList = new AlarmList(alarms);
        recyclerView.setAdapter(alarmList);

        // Adds a touch listener that handles selecting alarms for the RecyclerView
        recyclerView.addOnItemTouchListener(new ItemClicker(this, recyclerView, new ItemClicker.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Sets the selected alarm when an item is clicked and displays a toast that shows the selected alarm
                selectedAlarm = alarms.get(position);
                Toast.makeText(AlarmDeleteActivity.this, "Selected: " + selectedAlarm.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Optional: Implement behavior for long item click if needed
            }
        }));

        // Sets listener for the confirm button to pass the selected alarm details for confirmation
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAlarm != null) {
                    // If an alarm is selected, it then passes its details to the confirmation activity
                    confirmLink.putExtra("alarm_name", selectedAlarm.getName());
                    confirmLink.putExtra("alarm_time", selectedAlarm.getTime());
                    confirmLink.putExtra("alarm_am_pm", selectedAlarm.getAmPm());
                    confirmLink.putExtra("user", currentUser);
                    startActivity(confirmLink); // Goes to AlarmConfirmationActivity
                } else {
                    // Shows a toast message if no alarm is selected
                    Toast.makeText(view.getContext(), "Please select an alarm to delete", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Sets listener for the back button to go back to AlarmActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Goes back to AlarmActivity when the back button is pressed
                alarmLink.putExtra("user", currentUser);
                startActivity(alarmLink);
            }
        });
    }

    /**
     * Loads all the alarms from the CSV and populates the alarm list.
     * Displays a toast message if loading the alarms failed.
     */
    private void loadAlarmsFromCsv() {
        alarms.clear(); // Clears the list before loading in new data
        try (FileInputStream fis = openFileInput(ALARM_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            // Reads each line from the CSV and parses the alarm information
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    // Creates a new Alarm and adds it to the list
                    alarms.add(new Alarm(parts[0], parts[1], parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            // Logs an error message if it failed to load
            Log.e(TAG, "Failed to load alarms from CSV", e);
            // Shows a toast message indicating that it failed to load alarms
            Toast.makeText(this, "Failed to load alarms from CSV", Toast.LENGTH_LONG).show();
        }
    }
}