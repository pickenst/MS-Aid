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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.utsa.cs3443.msaid.model.User;

/**
 * @author Trey Pickens
 * ProfileActivity is where an user may view their information
 */
public class ProfileActivity extends AppCompatActivity {

    private User currentUser;
    private String lastUserDir = "last.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        Intent homeLink = new Intent(this, HomeActivity.class);
        Intent startLink = new Intent(this, MainActivity.class);
        startLink.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentUser = intent.getParcelableExtra("user");
        homeLink.putExtra("user", currentUser);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeLink);
            }
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File lastUser = new File(getFilesDir(), lastUserDir);
                lastUser.delete();
                startActivity(startLink);
            }
        });

        TextView nameDisplay = findViewById(R.id.name_display);
        nameDisplay.setText("Name:\n" + currentUser.getName());
        TextView ageDisplay = findViewById(R.id.age_display);
        ageDisplay.setText("Age: " + Integer.toString(currentUser.getAge()));
    }
}