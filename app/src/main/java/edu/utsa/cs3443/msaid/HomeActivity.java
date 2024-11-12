package edu.utsa.cs3443.msaid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import edu.utsa.cs3443.msaid.model.WeatherService;

public class HomeActivity extends AppCompatActivity {
    /*
        TODO:
         -Solve bug on first Activity launch on non-emulator devices
         -Implement visual functionality of WeatherService
         -Implement the User's Alarms array
         -Add User object to class that can be added to next activity (Or get the User from the previous activity)
         -_HEAVY_ Cleanup
         -Create ProfileActivity and add Intent
     */

    private static double longitude;
    private static double latitude;
    private WeatherService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addButtons();
        requestLocationPermissions();
        getLocation();
    }

    private void addButtons() {
        Button alarmsButton = findViewById(R.id.alarms_button);
        Button medicationButton = findViewById(R.id.medication_button);
        Button profileButton = findViewById(R.id.profile_button);
        Intent alarmLink = new Intent(this, AlarmActivity.class);
        Intent medicineLink = new Intent(this, MedicineActivity.class);

        alarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Moving to Alarms", Toast.LENGTH_LONG).show();
                startActivity(alarmLink);
            }
        });
        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Medication Button Pressed", Toast.LENGTH_LONG).show();
                startActivity(medicineLink);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Profile Button Pressed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestLocationPermissions() {
        //Request location permissions if application does not possess them
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void getLocation(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                initWeatherService();
            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, listener);
    }

    private void updateWeatherData() {
        if(service != null){
            try {
                service.setLocation(latitude, longitude);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        printWeatherData();
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initWeatherService(){
        try{
            service = new WeatherService(latitude, longitude, this);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateWeatherData();
                }
            }, 3000);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void printWeatherData(){
        if(service != null){
            System.out.println("" + service.getTemperature() + " " + service.getWindType() + " " + service.getWeather());

            Toast.makeText(this, "Latitude: " + latitude + "Longitude: " + longitude, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Temperature: " + service.getTemperature(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Weather: " + service.getWeather(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Wind Speed: " + service.getWindSpeed(), Toast.LENGTH_LONG).show();
        }
    }
}