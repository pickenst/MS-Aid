package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import edu.utsa.cs3443.msaid.model.User;

public class MedicationDetailActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_detail);
        currentUser = getIntent().getParcelableExtra("user");
        // Back button to return to MedicineActivity
        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicationDetailActivity.this, MedicineActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
                finish(); // Optionally finish the current activity to remove it from the back stack
            }
        });
    }
}
