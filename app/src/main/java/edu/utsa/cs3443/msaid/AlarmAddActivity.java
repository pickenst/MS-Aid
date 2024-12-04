package edu.utsa.cs3443.msaid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 * AlarmAddActivity allows users to add new alarms.
 * Users can input alarm name, time, and AM or PM to schedule an alarm.
 * The activity also saves the alarm information to a CSV file name alarms.csv.
 */
public class AlarmAddActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";
    private static final String TAG = "AlarmAddActivity"; // Defines a tag for the log

    /**
     * Called when the activity is created. Sets up the UI and the click listener for the confirm button.
     *
     * @param savedInstanceState If the activity is being reinitialized after previously being shut down,
     * this Bundle contains the data it was most recently given.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        // Get references for the UI elements
        EditText nameInput = findViewById(R.id.alarmInput); // Input for the alarm name
        EditText timeInput = findViewById(R.id.timeInput); // Input for the alarm time (hh:mm)
        EditText amPmInput = findViewById(R.id.amPmInput); // Input for the AM/PM value

        Button confirmButton = findViewById(R.id.confirm_button);

        // Set listener for the confirm button
        confirmButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString(); // Gets the alarm name from input
            String time = timeInput.getText().toString(); // Gets the alarm time from input
            String amPm = amPmInput.getText().toString().toUpperCase(); // Gets the AM/PM and converts it to uppercase

            // Verifies the input fields
            if (name.isEmpty() || !time.matches("^([1-9]|1[0-2]):[0-5][0-9]$") || (!amPm.equals("AM") && !amPm.equals("PM"))) {
                // Shows an error message if inputs are invalid
                Toast.makeText(view.getContext(), "Please enter valid alarm details", Toast.LENGTH_LONG).show();
            } else {
                // Saves the alarm information to the alarm CSV file
                saveAlarm(name, time, amPm);
                // Sets the alarm using AlarmManager
                setExactAlarm(name, time, amPm);
                // Shows a success message and finishes the activity
                Toast.makeText(view.getContext(), "Alarm added", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * Saves the alarm information to the alarm CSV.
     *
     * @param name The name of the alarm.
     * @param time The time of the alarm set in a hh:mm format.
     * @param amPm The AM/PM value for the alarm time.
     */
    private void saveAlarm(String name, String time, String amPm) {
        try (FileOutputStream fos = openFileOutput(ALARM_FILE, MODE_APPEND);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            // Writes the alarm info in a CSV format
            writer.write(name + "," + time + "," + amPm + "\n");
        } catch (Exception e) {
            // Logs an error message in case saving fails
            Log.e(TAG, "Failed to save alarm", e);
            // Shows a toast message in case of failure
            Toast.makeText(this, "Failed to save alarm", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets an exact alarm using the AlarmManager.
     *
     * @param name The name of the alarm.
     * @param time The time of the alarm set in a hh:mm format.
     * @param amPm The AM/PM for the alarm time.
     */
    private void setExactAlarm(String name, String time, String amPm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // Gets the AlarmManager service
        Intent intent = new Intent(this, AlarmReceiver.class); // Creates an intent for AlarmReceiver
        intent.putExtra("alarm_name", name); // Attaches the alarm name

        // Creates a PendingIntent for the alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, name.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Sets the alarm time using Calendar
        Calendar calendar = Calendar.getInstance();
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]); // Extracts the hour from the inputted time
        int minute = Integer.parseInt(timeParts[1]); // Extracts the minute from the inputted time

        // Converts the time into a 24-hour format
        if (amPm.equals("PM") && hour != 12) hour += 12; // All PM times, except for 12 PM, need to be converted
        if (amPm.equals("AM") && hour == 12) hour = 0; // 12 AM should be 0 in a 24-hour time format

        // Sets the calendar time to the specified hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Ensures that the alarm time is set in the future
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // If alarm time is in the past, it will set it for the next day
        }

        // Checks if exact alarm permissions are allowed
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Shows a toast message and prompts the user to enable exact alarm permissions in system settings
                Toast.makeText(this, "Enable exact alarm permissions in system settings", Toast.LENGTH_LONG).show();
                Intent permissionIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(permissionIntent);
                return;
            }
        }

        // Tries to set the exact alarm
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (SecurityException e) {
            // Logs an error message if setting the alarm fails due to lack of permissions
            Log.e(TAG, "Failed to set exact alarm. Check permissions.", e);
            // Shows a toast message indicating that it failed
            Toast.makeText(this, "Failed to set exact alarm. Check permissions.", Toast.LENGTH_LONG).show();
        }
    }
}