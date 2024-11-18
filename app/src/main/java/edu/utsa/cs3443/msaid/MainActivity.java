package edu.utsa.cs3443.msaid;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import edu.utsa.cs3443.msaid.model.User;

public class MainActivity extends AppCompatActivity {

    /*
        TODO:
         -Cleanup
     */
    private Intent loginLink;
    private Intent signupLink;
    String userLoginDir = "userLogin.csv";
    String lastUserDir = "last.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestLocationPermissions();
        try {
            resumeUser();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        loginLink = new Intent(this, LoginActivity.class);
        signupLink = new Intent(this, SignupActivityOne.class);
        validateFiles();
        initButtons();
    }

    private void initButtons(){
        Button loginButton = findViewById(R.id.login);
        Button signupButton = findViewById(R.id.signup);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(loginLink);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(signupLink);
            }
        });
    }

    private void validateFiles(){
        File userLogin = new File(getFilesDir(), userLoginDir);
        if(!userLogin.exists()){
            try {
                FileOutputStream creator = new FileOutputStream(userLogin, true);
            } catch (FileNotFoundException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void resumeUser() throws FileNotFoundException {
        Intent homeLink = new Intent(this, HomeActivity.class);
        File lastUser = new File(getFilesDir(), lastUserDir);
        if(!lastUser.exists()){
            return;
        }
        InputStream in = this.openFileInput(lastUserDir);
        Scanner scnr = new Scanner(in);
        String[] buffer = scnr.nextLine().split(",");
        homeLink.putExtra("user", new User(buffer[0], Integer.parseInt(buffer[1])));
        startActivity(homeLink);
    }

    private void requestLocationPermissions() {
        //Request location permissions if application does not possess them
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
}