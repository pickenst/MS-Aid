package edu.utsa.cs3443.msaid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    /*
        TODO: Cleanup
     */
    String userLoginDir = "userLogin.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent loginLink = new Intent(this, LoginActivity.class);
        Intent signupLink = new Intent(this, SignupActivityOne.class);
        File userLogin = new File(getFilesDir(), userLoginDir);
        if(!userLogin.exists()) {
            try {
                FileOutputStream creator = new FileOutputStream(userLogin, true);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Button loginButton = findViewById(R.id.login);
        Button signupButton = findViewById(R.id.signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Login Screen", Toast.LENGTH_LONG).show();

                startActivity(loginLink);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Sign Up Screen", Toast.LENGTH_LONG).show();

                startActivity(signupLink);
            }
        });

    }
}