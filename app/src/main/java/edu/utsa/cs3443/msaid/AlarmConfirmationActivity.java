package edu.utsa.cs3443.msaid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class AlarmConfirmationActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
            TODO: Add deletion logic
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_confirmation);
        currentUser = getIntent().getParcelableExtra("user");
        Intent yesLink = new Intent(this, AlarmActivity.class);
        yesLink.putExtra("user", currentUser);
        Intent noLink = new Intent(this, AlarmDeleteActivity.class);
        noLink.putExtra("user", currentUser);
        Intent deleteLink = new Intent(this, AlarmDeleteActivity.class);
        deleteLink.putExtra("user", currentUser);


        Button yesButton = findViewById(R.id.yes_button);
        Button noButton = findViewById(R.id.no_button);
        Button backButton = findViewById(R.id.back_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Alarm Deleted", Toast.LENGTH_LONG).show();
                startActivity(yesLink);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Delete Cancelled", Toast.LENGTH_LONG).show();
                startActivity(noLink);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving back to Delete Alarms", Toast.LENGTH_LONG).show();
                startActivity(deleteLink);
            }
        });

    }
}
