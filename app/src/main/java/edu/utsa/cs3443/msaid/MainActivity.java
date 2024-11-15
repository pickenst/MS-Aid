package edu.utsa.cs3443.msaid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
        requestLocationPermissions();
        Intent loginLink = new Intent(this, LoginActivity.class);
        Intent signupLink = new Intent(this, SignupActivityOne.class);
        validateFiles();
        initButtons(loginLink, signupLink);
    }

    private void initButtons(Intent loginLink, Intent signupLink){
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

    private void requestLocationPermissions() {
        //Request location permissions if application does not possess them
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
}