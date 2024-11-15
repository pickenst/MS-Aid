package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class MedicineActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        currentUser = getIntent().getParcelableExtra("user");
        // Initialize buttons
        Button backButton = findViewById(R.id.back);
        Button addButton = findViewById(R.id.add);
        Button removeButton = findViewById(R.id.remove);
        Button specificMedButton = findViewById(R.id.Test);

        // Set OnClickListener for the Back button to go back to HomeActivity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineActivity.this, HomeActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Finish current activity
            }
        });

        // Set OnClickListener for the Add button to go to AddMedicationActivity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineActivity.this, AddMedicineActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the Remove button to go to DeleteMedicationActivity
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineActivity.this, DeleteMedicationActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the specific medication button (Fingolimod) to go to MedicationDetailActivity
        specificMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicineActivity.this, MedicationDetailActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }
}
