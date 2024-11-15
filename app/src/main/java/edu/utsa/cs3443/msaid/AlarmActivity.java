package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class AlarmActivity extends AppCompatActivity {

    private User currentUser;

    /*
        TODO: Add functionality to load alarms as TextViews
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        currentUser = getIntent().getParcelableExtra("user");
        Intent homeLink = new Intent(this, HomeActivity.class);
        homeLink.putExtra("user", currentUser);
        Intent addLink = new Intent(this, AlarmAddActivity.class);
        addLink.putExtra("user", currentUser);
        Intent deleteLink = new Intent(this, AlarmDeleteActivity.class);
        deleteLink.putExtra("user", currentUser);


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

