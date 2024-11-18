package edu.utsa.cs3443.msaid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class AlarmAddActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        Intent alarmLink = new Intent(this, AlarmActivity.class);
        Intent homeLink = new Intent(this, AlarmActivity.class);

        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_button);

        EditText nameInput = findViewById(R.id.alarmInput);
        EditText timeInput = findViewById(R.id.timeInput);
        EditText amPmInput = findViewById(R.id.amPmInput);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String time = timeInput.getText().toString();
                String amPm = amPmInput.getText().toString().toUpperCase();

                if (name.isEmpty() || !time.matches("^([1-9]|1[0-2]):[0-5][0-9]$") || (!amPm.equals("AM") && !amPm.equals("PM"))) {
                    Toast.makeText(view.getContext(), "Please enter valid alarm details", Toast.LENGTH_LONG).show();
                } else {
                    saveAlarm(name, time, amPm);
                    if (canScheduleExactAlarms()) {
                        try {
                            setAlarm(name, time, amPm);
                            Toast.makeText(view.getContext(), "Alarm added", Toast.LENGTH_LONG).show();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                            Toast.makeText(view.getContext(), "Failed to set alarm due to lack of permission", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        requestExactAlarmPermission();
                    }

                    startActivity(alarmLink);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving back to Alarms", Toast.LENGTH_LONG).show();
                startActivity(homeLink);
            }
        });
    }

    private void saveAlarm(String name, String time, String amPm) {
        try (FileOutputStream fos = openFileOutput(ALARM_FILE, MODE_APPEND);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            String alarmData = name + "," + time + "," + amPm + "\n";
            writer.write(alarmData);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save alarm", Toast.LENGTH_LONG).show();
        }
    }

    private void setAlarm(String name, String time, String amPm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("alarm_name", name);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        if (amPm.equals("PM") && hour != 12) {
            hour += 12;
        } else if (amPm.equals("AM") && hour == 12) {
            hour = 0;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        long alarmTimeInMillis = calendar.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent);
    }

    private boolean canScheduleExactAlarms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            return alarmManager.canScheduleExactAlarms();
        }
        return true; 
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }
    }
}
