package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddMedicineActivity extends AppCompatActivity {

    // Declare EditText and Button variables
    private EditText nameEditText;
    private EditText instructionsEditText;
    private Button backButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication); // Set the layout file

        // Initialize EditText and Button views
        nameEditText = findViewById(R.id.nameEditText);
        instructionsEditText = findViewById(R.id.instructionsEditText);
        backButton = findViewById(R.id.Back);
        addButton = findViewById(R.id.add);

        // Back Button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Medicine Activity
                Intent intent = new Intent(AddMedicineActivity.this, MedicineActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to remove it from the stack
            }
        });

        // Add Button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from EditText fields
                String name = nameEditText.getText().toString();
                String instructions = instructionsEditText.getText().toString();

                // You can process the data here, e.g., saving it to a database

                // Navigate back to Medicine Activity
                Intent intent = new Intent(AddMedicineActivity.this, MedicineActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to remove it from the stack
            }
        });
    }
}
