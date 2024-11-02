package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
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
}

