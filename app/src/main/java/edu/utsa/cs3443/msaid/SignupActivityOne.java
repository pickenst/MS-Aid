package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivityOne extends AppCompatActivity {
    /*
        TODO: Get items from TextView xml objects and add them to next Intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_one);
        Intent continueSignup = new Intent(this, SignupActivityTwo.class);

        Button signupButton = findViewById(R.id.complete_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Sign Up Pressed", Toast.LENGTH_LONG).show();

                startActivity(continueSignup);
            }
        });

    }
}