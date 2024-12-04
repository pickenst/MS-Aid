package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;

/**
 * MedicationConfirmationActivity handles the confirmation process
 * for deleting a selected medicine.
 * It displays a confirmation message and provides options to confirm
 * or cancel the deletion.
 */
public class MedicationConfirmationActivity extends AppCompatActivity {

    private Medicine selectedMedicine;
    private User currentUser;

    /**
     * Called when the activity is starting. Sets up the UI and handles
     * the delete confirmation or cancellation process.
     *
     * @param savedInstanceState If the activity is being re-initialized
     *                           after previously being shut down, this Bundle
     *                           contains the data most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_confirmation);

        // Retrieve the selected medicine and current user
        selectedMedicine = getIntent().getParcelableExtra("medicine");
        currentUser = getIntent().getParcelableExtra("user");

        if (selectedMedicine == null || currentUser == null) {
            Log.e("MedicationConfirmation", "Medicine or user is null!");
            finish();
            return;
        }

        // Set up UI
        TextView confirmationTextView = findViewById(R.id.confirmationTextView);
        Button confirmDeleteButton = findViewById(R.id.confirmDeleteButton);
        Button cancelDeleteButton = findViewById(R.id.cancelDeleteButton);

        // Display a confirmation message
        confirmationTextView.setText("Are you sure you want to delete " + selectedMedicine.getName() + "?");

        // Handle delete confirmation
        confirmDeleteButton.setOnClickListener(view -> deleteMedicineAndNavigate());

        // Handle cancel action
        cancelDeleteButton.setOnClickListener(view -> finish());
    }

    /**
     * Deletes the selected medicine from the user's list and navigates back
     * to the MedicineActivity. Logs the deletion status.
     */
    private void deleteMedicineAndNavigate() {
        if (currentUser.getMedicines().remove(selectedMedicine)) {
            // If persistence is required, write changes to a database or file here
            Log.i("MedicationConfirmation", "Medicine deleted: " + selectedMedicine.getName());

            // Navigate back to MedicineActivity
            Intent intent = new Intent(this, MedicineActivity.class);
            intent.putExtra("user", currentUser); // Pass updated user object
            startActivity(intent);
            finish();
        } else {
            Log.e("MedicationConfirmation", "Failed to delete medicine.");
        }
    }
}