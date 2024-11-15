package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class DeleteMedicationActivity extends AppCompatActivity {

    private Button backButton;
    private Button testButton;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_medication); // Set the layout file
        currentUser = getIntent().getParcelableExtra("user");
        // Initialize buttons
        backButton = findViewById(R.id.back);
        testButton = findViewById(R.id.Test);

        // Set the OnClickListener for the Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MedicineActivity
                Intent intent = new Intent(DeleteMedicationActivity.this, MedicineActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Finish the current activity to remove it from the stack
            }
        });

        // Set the OnClickListener for the Test button (this will lead to the "Are You Sure" screen)
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the "Are You Sure" confirmation screen
                Intent intent = new Intent(DeleteMedicationActivity.this, MedicationConfirmationActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }
}
