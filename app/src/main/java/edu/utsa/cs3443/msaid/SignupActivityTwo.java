package edu.utsa.cs3443.msaid;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import edu.utsa.cs3443.msaid.model.User;

public class SignupActivityTwo extends AppCompatActivity {
    /*
        TODO:
         -Create a new User directory containing user info named after user
     */

    String usersDir = "users";
    String userLoginDir = "userLogin.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_two);


        RadioButton noRadio = findViewById(R.id.no_radio);
        noRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.control_panel_red));
        RadioButton yesRadio = findViewById(R.id.yes_radio);
        yesRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.control_panel_red));

        Intent loginLink = new Intent(this, LoginActivity.class);
        Intent intent = getIntent();

        EditText firstNameInput = findViewById(R.id.first_name_input);
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText ageInput = findViewById(R.id.age_input);

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        File users = new File(getFilesDir(), usersDir);
        File userLogin = new File(getFilesDir(), userLoginDir);
        if(!users.exists()){
            boolean created = users.mkdir();
        }

        Button signupButton = findViewById(R.id.complete_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameInput.getText().toString();
                String lastName = lastNameInput.getText().toString();
                String age = ageInput.getText().toString();
                String inBuffer = username + "," + password + "," + firstName + "," + lastName + "," + age + "\n";
                try {
                    FileOutputStream writer = new FileOutputStream(userLogin, true);
                    writer.write(inBuffer.getBytes(StandardCharsets.UTF_8));
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                startActivity(loginLink);
            }
        });

    }
}