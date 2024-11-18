package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AlarmActivity extends AppCompatActivity {
    private ArrayList<Alarm> alarms;
    private AlarmList alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);

        Intent homeLink = new Intent(this, HomeActivity.class);
        Intent addLink = new Intent(this, AlarmAddActivity.class);
        Intent deleteLink = new Intent(this, AlarmDeleteActivity.class);

        Button addButton = findViewById(R.id.add_button);
        Button deleteButton = findViewById(R.id.delete_button);
        Button backButton = findViewById(R.id.back_button);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        alarmList = new AlarmList(alarms);
        recyclerView.setAdapter(alarmList);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Add Alarms", Toast.LENGTH_LONG).show();
                startActivity(addLink);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Delete Alarms", Toast.LENGTH_LONG).show();
                startActivity(deleteLink);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving back to Home", Toast.LENGTH_LONG).show();
                startActivity(homeLink);
            }
        });
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

