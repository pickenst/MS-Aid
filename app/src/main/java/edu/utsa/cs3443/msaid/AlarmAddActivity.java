package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmAddActivity extends AppCompatActivity {
    /*
        TODO: Add functionality combined with the User and Alarm classes
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        Intent alarmLink = new Intent(this, AlarmActivity.class);
        Intent homeLink = new Intent(this, AlarmActivity.class);


        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Alarm added", Toast.LENGTH_LONG).show();
                startActivity(alarmLink);
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
}