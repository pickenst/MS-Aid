package edu.utsa.cs3443.msaid;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmDeleteActivity extends AppCompatActivity {
    /*
        TODO: Take basic structure of Activity and, when delete
         button in AlarmActivity is pressed, it switches to a view that looks like this on the _AlarmActivity_
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_delete);
        Intent confirmLink = new Intent(this, AlarmConfirmationActivity.class);
        Intent alarmLink = new Intent(this, AlarmActivity.class);


        Button confirmButton = findViewById(R.id.confirm_button);
        Button backButton = findViewById(R.id.back_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Confirmation", Toast.LENGTH_LONG).show();
                startActivity(confirmLink);
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
}
