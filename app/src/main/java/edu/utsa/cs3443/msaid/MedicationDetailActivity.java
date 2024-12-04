package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;

/**
 * MedicationDetailActivity displays detailed information about a specific medicine
 * and allows users to navigate back to the previous screen while ensuring
 * data consistency.
 */
public class MedicationDetailActivity extends AppCompatActivity {

    private TextView medicineInfoTextView;
    private Button backButton;

    /**
     * Called when the activity is starting. Initializes the UI components,
     * retrieves the passed Medicine and User objects, and handles back navigation.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the data most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_detail);

        // Initialize the TextView for displaying the medicine info
        medicineInfoTextView = findViewById(R.id.medicineInfo);

        // Get the Medicine object passed from the previous activity
        Intent intent = getIntent();
        Medicine medicine = intent.getParcelableExtra("medicine");

        if (medicine != null) {
            // Display the medicine information
            medicineInfoTextView.setText(medicine.getInfo());
        } else {
            // If no medicine details are available
            medicineInfoTextView.setText("No medication details available.");
        }

        // Back button setup
        backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> {
            // Handle back explicitly without using onBackPressed()
            Intent resultIntent = new Intent();

            // Retrieve the user object from the current Intent
            User user = getIntent().getParcelableExtra("user");

            // Ensure we are passing the user data back
            if (user != null) {
                resultIntent.putExtra("user", user);
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }

            // Set the result to pass the data back
            setResult(RESULT_OK, resultIntent);

            // Close the current activity and return to the MedicineActivity
            finish();
        });
    }
}