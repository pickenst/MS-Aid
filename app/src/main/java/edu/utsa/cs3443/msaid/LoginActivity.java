package edu.utsa.cs3443.msaid;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import edu.utsa.cs3443.msaid.model.User;

/**
 * @author Trey Pickens
 * The Login Activity is where the user can login to their profile from
 */
public class LoginActivity extends AppCompatActivity {

    String usersDir = "users";
    String userLoginDir = "userLogin.csv";
    String lastUserDir = "last.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usernameInput = findViewById(R.id.username);
        EditText passwordInput = findViewById(R.id.password);
        CheckBox rememberMeBox = findViewById(R.id.remember_me);
        Intent homeLink = new Intent(this, HomeActivity.class);
        homeLink.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        File users = new File(getFilesDir(), usersDir);
        InputStream in;
        Scanner scnr;
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
                Boolean foundUser = false;
                while(finalScnr.hasNext()){
                    String[] buffer = (finalScnr.next()).split(",");
                    if(buffer[0].equals(username) && buffer[1].equals(password)){
                        User activeUser = new User(buffer[2] + " " + buffer[3], parseInt(buffer[4]));
                        if(rememberMeBox.isChecked()){
                            File lastUser = new File(getFilesDir(), lastUserDir);
                            try {
                                FileOutputStream writer = new FileOutputStream(lastUser, false);
                                writer.write((activeUser.getName() + "," + activeUser.getAge()).getBytes());
                                writer.close();
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        homeLink.putExtra("user", activeUser);
                        startActivity(homeLink);
                        foundUser = true;
                        finish();
                        break;
                    }
                }
                if(!foundUser){
                    Toast.makeText(view.getContext(), "Username and/or password is incorrect.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}