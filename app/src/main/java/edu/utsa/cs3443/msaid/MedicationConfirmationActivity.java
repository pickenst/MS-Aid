package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class MedicationConfirmationActivity extends AppCompatActivity {

    private Button yesButton;
    private Button noButton;
    private Button backButton;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_confirmation); // Set the layout file
        currentUser = getIntent().getParcelableExtra("user");
        // Initialize buttons
        yesButton = findViewById(R.id.yes);
        noButton = findViewById(R.id.no);
        backButton = findViewById(R.id.back);

        // "Yes" button: navigate to MedicineActivity
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for deleting the medication here (if needed)
                Intent intent = new Intent(MedicationConfirmationActivity.this, MedicineActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Optionally finish this activity after starting the new one
            }
        });

        // "No" and "Back" buttons: navigate back to DeleteMedicationActivity
        View.OnClickListener goBackListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicationConfirmationActivity.this, DeleteMedicationActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        };

        noButton.setOnClickListener(goBackListener);
        backButton.setOnClickListener(goBackListener);
    }
}
