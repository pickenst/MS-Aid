package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;

/**
 * MedicineActivity manages the list of medicines associated with a user.
 * It provides functionality to view, add, and remove medicines.
 */
public class MedicineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MEDICATION = 100;
    private static final int REQUEST_CODE_DELETE_MEDICATION = 101;
    private User currentUser;
    private RecyclerView medicineRecyclerView;
    private MedicationAdapter adapter;

    /**
     * Called when the activity is starting. Initializes the UI components, retrieves the current
     * user data, and sets up the RecyclerView for displaying medicines.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the data most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        // Initialize currentUser object from the intent
        currentUser = getIntent().getParcelableExtra("user");
        Intent homeLink = new Intent(this, HomeActivity.class);
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeLink);
            }
        });
        if (currentUser != null) {
            Log.d("MedicineActivity", "Received currentUser: " + currentUser.toString());

            // Initialize RecyclerView and set up the adapter
            medicineRecyclerView = findViewById(R.id.medicineRecyclerView);
            medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new MedicationAdapter(this, currentUser.getMedicines(), null, currentUser, false);
            medicineRecyclerView.setAdapter(adapter);

            // Update the adapter with the current list of medicines
            adapter.updateMedicines(currentUser.getMedicines());
        } else {
            Log.e("MedicineActivity", "Failed to receive currentUser.");
            Toast.makeText(this, "Failed to load medicine list", Toast.LENGTH_SHORT).show();
        }

        // Set up Add button
        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MedicineActivity.this, AddMedicineActivity.class);
            intent.putExtra("user", currentUser);  // Pass the current user
            startActivityForResult(intent, REQUEST_CODE_ADD_MEDICATION);
        });

        // Set up Delete button
        Button deleteButton = findViewById(R.id.remove);
        deleteButton.setOnClickListener(view -> {
            Intent intent = new Intent(MedicineActivity.this, DeleteMedicationActivity.class);
            intent.putExtra("user", currentUser);  // Pass the current user
            startActivityForResult(intent, REQUEST_CODE_DELETE_MEDICATION);
        });
    }

    /**
     * Handles the results from other activities. Updates the medicine list
     * when returning from AddMedicineActivity or DeleteMedicationActivity.
     *
     * @param requestCode The request code passed to startActivityForResult.
     * @param resultCode The result code returned by the child activity.
     * @param data The intent containing the updated user data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is OK
        if (resultCode == RESULT_OK && data != null) {
            // Get the updated user object
            User user = data.getParcelableExtra("user");

            if (user != null) {
                // Update the adapter with the new user data
                currentUser = user;
                adapter.updateMedicines(currentUser.getMedicines());  // Refresh the RecyclerView
            } else {
                Toast.makeText(this, "Failed to update medicine list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Ensures the medicine data is reloaded when the activity resumes. This is necessary
     * to reflect any changes made in other activities.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Ensure the data is reloaded when returning from another activity
        if (currentUser != null) {
            adapter.updateMedicines(currentUser.getMedicines());
        }
    }
}