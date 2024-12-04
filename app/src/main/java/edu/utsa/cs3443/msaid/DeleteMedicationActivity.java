package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;
import edu.utsa.cs3443.msaid.MedicationAdapter;
import java.util.List;
import java.util.ArrayList;

/**
 * Activity for deleting medications from a user's list.
 * Displays a list of medications in a RecyclerView and allows the user to select a medication to delete.
 */
public class DeleteMedicationActivity extends AppCompatActivity implements MedicationAdapter.OnMedicineClickListener {

    /** Request code for confirming a medication deletion. */
    private static final int REQUEST_CODE_CONFIRM_DELETE = 100;

    private User currentUser; // Current user interacting with the app
    private RecyclerView medicineRecyclerView; // RecyclerView for displaying the list of medicines
    private List<Medicine> medicines; // List of medicines
    private edu.utsa.cs3443.msaid.MedicationAdapter adapter; // Adapter for managing the RecyclerView

    /**
     * Initializes the activity, sets up the RecyclerView, and loads the user's medication list.
     *
     * @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_medication);

        // Retrieve the currentUser object passed from the previous activity
        currentUser = getIntent().getParcelableExtra("user");

        if (currentUser != null) {
            // Initialize RecyclerView
            medicineRecyclerView = findViewById(R.id.deleteMedicineRecyclerView);
            medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Get the list of medicines from the User object
            medicines = currentUser.getMedicines();

            // Create and set the adapter for the RecyclerView
            adapter = new MedicationAdapter(this, medicines, this, currentUser, true);  // Pass true for delete page
            medicineRecyclerView.setAdapter(adapter);
        } else {
            Log.e("DeleteMedicationActivity", "Current user is null!");
        }

        // Initialize Back button
        Button backButton = findViewById(R.id.back);

        // Set OnClickListener for the Back button
        backButton.setOnClickListener(view -> {
            if (currentUser != null) {
                Intent intent = new Intent(DeleteMedicationActivity.this, MedicineActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish();
            } else {
                Log.e("DeleteMedicationActivity", "Unable to navigate, current user is null");
            }
        });
    }

    /**
     * Handles a click on a medicine item in the RecyclerView.
     * Navigates to the MedicationConfirmationActivity to confirm deletion.
     *
     * @param medicine the selected medicine to delete
     */
    @Override
    public void onMedicineClick(Medicine medicine) {
        // Navigate to MedicationConfirmationActivity
        Intent intent = new Intent(DeleteMedicationActivity.this, MedicationConfirmationActivity.class);
        intent.putExtra("medicine", medicine);
        intent.putExtra("user", currentUser);
        startActivityForResult(intent, REQUEST_CODE_CONFIRM_DELETE); // Start the activity for result
    }

    /**
     * Handles the result from the confirmation activity.
     * Updates the medicine list and sends the updated list back to the calling activity.
     *
     * @param requestCode the request code of the returned result
     * @param resultCode the result code of the returned result
     * @param data the intent containing the result data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CONFIRM_DELETE && resultCode == RESULT_OK) {
            // Retrieve the updated user object from the result
            User updatedUser = data.getParcelableExtra("user");

            if (updatedUser != null) {
                // Update the medicines list with the new list from the user object
                medicines = updatedUser.getMedicines();
                adapter.updateMedicines(medicines);  // Update the adapter to refresh the list

                // Send the updated list back to MedicineActivity
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra("updated_medicines", new ArrayList<>(medicines));
                setResult(RESULT_OK, resultIntent);  // Set the result to OK (success)
                finish(); // Close the activity
            }
        }
    }
}