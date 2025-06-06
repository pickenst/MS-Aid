package edu.utsa.cs3443.msaid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author Trey Pickens
 * SignupActivityOne is the first of two signup Activities to create a new profile
 */
public class SignupActivityOne extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_one);
        Intent continueSignup = new Intent(this, SignupActivityTwo.class);
        initViews(continueSignup);
    }

    /**
     * Initializes the views in the Activity
     * @param continueSignup the Intent for the next sign up screen (Intent)
     */
    private void initViews(Intent continueSignup){
        Button signupButton = findViewById(R.id.complete_signup);
        EditText usernameInput = findViewById(R.id.username_input);
        EditText passwordInput = findViewById(R.id.password_input);
        EditText confirmInput = findViewById(R.id.confirm_password_input);

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmInput.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(view.getContext(), "Username field cannot be blank.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(view.getContext(), "Password field cannot be blank.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!(password.equals(confirmPassword))){
                    Toast.makeText(view.getContext(), "Passwords must match.", Toast.LENGTH_LONG).show();
                    return;
                }
                continueSignup.putExtra("username", username);
                continueSignup.putExtra("password", password);
                startActivity(continueSignup);
            }
        });
    }
}