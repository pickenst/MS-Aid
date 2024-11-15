package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.utsa.cs3443.msaid.model.User;

public class ProfileActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
            TODO:
             -Add Log Out button.
             -Add edit profile functionality.
             -Add profile picture or remove profile picture for more active space.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        Intent homeLink = new Intent(this, HomeActivity.class);
        currentUser = intent.getParcelableExtra("user");
        homeLink.putExtra("user", currentUser);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeLink);
            }
        });
        TextView nameDisplay = findViewById(R.id.name_display);
        nameDisplay.setText("Name:\n" + currentUser.getName());
        TextView ageDisplay = findViewById(R.id.age_display);
        ageDisplay.setText("Age: " + Integer.toString(currentUser.getAge()));
    }
}