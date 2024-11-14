package edu.utsa.cs3443.msaid;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import edu.utsa.cs3443.msaid.model.User;

public class LoginActivity extends AppCompatActivity {
    /*
        TODO:
         -Add basic functionality (Pulling data from an User file based on login and instantiating an User object)
     */

    String usersDir = "users";
    String userLoginDir = "userLogin.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usernameInput = findViewById(R.id.username);
        EditText passwordInput = findViewById(R.id.password);
        Intent homeLink = new Intent(this, HomeActivity.class);
        File users = new File(getFilesDir(), usersDir);
        File userLogin = new File(getFilesDir(), userLoginDir);
        OutputStream out = null;
        InputStream in = null;
        Scanner scnr = null;
        if(!users.exists()){
            boolean created = users.mkdir();
        }
        try {
            in = this.openFileInput(userLoginDir);
            scnr = new Scanner(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Button loginButton = findViewById(R.id.login);
        Scanner finalScnr = scnr;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                Toast.makeText(view.getContext(), "Login Pressed", Toast.LENGTH_LONG).show();
                Boolean foundUser = false;
                while(finalScnr.hasNext()){
                    String[] buffer = (finalScnr.next()).split(",");
                    if(buffer[0].equals(username) && buffer[1].equals(password)){
                        User activeUser = new User(buffer[2] + " " + buffer[3], parseInt(buffer[4]));
                        homeLink.putExtra("user", activeUser);
                        startActivity(homeLink);
                        foundUser = true;
                        break;
                    }
                }
                if(!foundUser){
                    Toast.makeText(view.getContext(), "Username and/or Password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}