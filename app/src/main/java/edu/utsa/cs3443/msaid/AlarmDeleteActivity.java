package edu.utsa.cs3443.msaid;

import android.os.Bundle;
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

public class AlarmDeleteActivity extends AppCompatActivity {
    private static final String ALARM_FILE = "alarms.csv";
    private ArrayList<Alarm> alarms;
    private Alarm selectedAlarm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_delete);

        Intent confirmLink = new Intent(this, AlarmConfirmationActivity.class);
        Intent alarmLink = new Intent(this, AlarmActivity.class);

        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_button);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        AlarmList alarmList = new AlarmList(alarms);
        recyclerView.setAdapter(alarmList);

        recyclerView.addOnItemTouchListener(new ItemClicker(this, recyclerView, new ItemClicker.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedAlarm = alarms.get(position);
                Toast.makeText(AlarmDeleteActivity.this, "Selected: " + selectedAlarm.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAlarm != null) {
                    confirmLink.putExtra("alarm_name", selectedAlarm.getName());
                    confirmLink.putExtra("alarm_time", selectedAlarm.getTime());
                    confirmLink.putExtra("alarm_am_pm", selectedAlarm.getAmPm());
                    startActivity(confirmLink);
                } else {
                    Toast.makeText(view.getContext(), "Please select an alarm to delete", Toast.LENGTH_LONG).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving back to Alarms", Toast.LENGTH_LONG).show();
                startActivity(alarmLink);
            }
        });
    }

    private void loadAlarmsFromCsv() {
        alarms.clear();
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

}
