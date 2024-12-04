package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;

/**
 * Activity to add a new medicine to a user's medication list.
 * Users can input the name and instructions for the medicine.
 */
public class AddMedicineActivity extends AppCompatActivity {

    private User currentUser; // The current user interacting with the app
    private EditText nameEditText; // Input field for the medicine name
    private EditText instructionsEditText; // Input field for the medicine instructions
    private Button addButton; // Button to add the medicine
    private Button backButton; // Button to return to the previous screen

    /**
     * Initializes the activity, sets up UI components, and handles intent data.
     *
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        // Get currentUser from intent
        currentUser = getIntent().getParcelableExtra("user");

        if (currentUser == null) {
            Log.e("AddMedicineActivity", "User object is null!");
            Toast.makeText(this, "User data missing, returning to previous screen.", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity if user is not passed correctly
            return;
        }

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        addButton = findViewById(R.id.add);
        backButton = findViewById(R.id.Back);

        // Handle Add Button click
        addButton.setOnClickListener(v -> {
            try {
                String name = nameEditText.getText().toString().trim();
                String instructions = instructionsEditText.getText().toString().trim();

                if (name.isEmpty() || instructions.isEmpty()) {
                    Log.e("AddMedicineActivity", "Name or instructions are empty!");
                    Toast.makeText(AddMedicineActivity.this, "Both fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Medicine newMedicine = new Medicine(name, instructions);
                currentUser.addMedicine(newMedicine);

                Log.d("AddMedicineActivity", "Medicine added: " + name);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("user", currentUser);
                setResult(RESULT_OK, resultIntent);
                finish();

            } catch (Exception e) {
                Log.e("AddMedicineActivity", "Error adding medicine: ", e);
            }
        });

        // Handle Back Button click
        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}