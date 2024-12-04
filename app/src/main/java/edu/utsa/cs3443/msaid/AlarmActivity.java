package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.utsa.cs3443.msaid.model.Alarm;
import edu.utsa.cs3443.msaid.AlarmList;
import edu.utsa.cs3443.msaid.model.User;

/**
 * AlarmActivity is responsible for displaying the main list of alarms
 * and provides the user with the option to add, delete, or go back to the home screen.
 * AlarmActivity uses a RecyclerView to show list the alarms and allows for interaction through buttons.
 */
public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";  // Defines a tag for the log
    private ArrayList<Alarm> alarms;  // List of alarms to be displayed
    private edu.utsa.cs3443.msaid.AlarmList alarmList;// The Adapter for binding the alarms to the RecyclerView

    /**
     * Called for when the activity is created. Initializes the views, sets up the RecyclerView,
     * and finally sets click listeners for add, delete, and back buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it has been most recently given.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        User currentUser;
        Intent last = getIntent();
        currentUser = last.getParcelableExtra("user");

        Intent homeLink = new Intent(this, HomeActivity.class);
        Intent addLink = new Intent(this, AlarmAddActivity.class);
        Intent deleteLink = new Intent(this, AlarmDeleteActivity.class);

        Button addButton = findViewById(R.id.add_button);
        Button deleteButton = findViewById(R.id.delete_button);
        Button backButton = findViewById(R.id.back_button);

        // Sets up the RecyclerView for the alarms with a linear layout
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializes the alarms list and loads the data from the CSV file
        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        // Sets up the adapter with the alarm list
        alarmList = new AlarmList(alarms);
        recyclerView.setAdapter(alarmList);

        // Sets click listener for the add button to start the AlarmAddActivity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLink.putExtra("user", currentUser);
                startActivity(addLink);
            }
        });

        // Sets click listener for the delete button to start the AlarmDeleteActivity
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLink.putExtra("user", currentUser);
                startActivity(deleteLink);
            }
        });

        // Sets click listener for the back button to return to the HomeActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeLink.putExtra("user", currentUser);
                startActivity(homeLink);
            }
        });
    }

    /**
     * Called when the activity is resumed. Reloads the alarms to ensure the data is updated.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Reloads the alarms from the alarm CSV when resuming activity to ensure the data is fully updated
        loadAlarmsFromCsv();
        alarmList.setAlarms(alarms); // Updates the adapter with the newly refreshed alarm list
    }

    /**
     * Loads the list of alarms from the CSV file. Clears the current list before loading to prevent duplicates.
     * Shows a Toast message if an error occurs while reading the file.
     */
    private void loadAlarmsFromCsv() {
        alarms.clear(); // Clears the current list to avoid duplicates and prevents bugs
        try (FileInputStream fis = openFileInput("alarms.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            // Reads each line from the alarm CSV file and parses through the alarm data
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    // Creates a new Alarm object and adds it to the alarm list
                    alarms.add(new Alarm(parts[0], parts[1], parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            // Logs the error and shows a toast message only if loading the alarm CSV fails
            Log.e(TAG, "Failed to load alarms from CSV", e);
            Toast.makeText(this, "Failed to load alarms from CSV", Toast.LENGTH_LONG).show();
        }
    }
}
