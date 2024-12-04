package edu.utsa.cs3443.msaid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.utsa.cs3443.msaid.model.Alarm;
import edu.utsa.cs3443.msaid.AlarmList;
import edu.utsa.cs3443.msaid.model.User;
import edu.utsa.cs3443.msaid.model.WeatherService;

/**
 * @author Trey Pickens
 * @author Antonio Valencia-Giles
 */
public class HomeActivity extends AppCompatActivity {
    private final int INIT_DELAY_TIME = 1250; //ms

    /*
        TODO:
         -Implement visual functionality of WeatherService
     */

    private static double longitude;
    private static double latitude;
    private WeatherService service;

    private TextView windDisplay;
    private TextView weatherDisplay;
    private TextView tempDisplay;
    private TextView warningDisplay;
    private User currentUser;

    private ArrayList<Alarm> alarms;
    private AlarmList alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        currentUser = getIntent().getParcelableExtra("user");
        windDisplay = findViewById(R.id.windy_textbox);
        weatherDisplay = findViewById(R.id.weather_textbox);
        tempDisplay = findViewById(R.id.temp_textbox);
        warningDisplay = findViewById(R.id.warning_textbox);
        addButtons();
        requestLocationPermissions();
        getLocation();

        RecyclerView alarmRecyclerView = findViewById(R.id.alarmRecyclerView);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        alarmList = new AlarmList(alarms);
        alarmRecyclerView.setAdapter(alarmList);
    }

    private void addButtons() {
        Button alarmsButton = findViewById(R.id.alarms_button);
        Button medicationButton = findViewById(R.id.medication_button);
        Button profileButton = findViewById(R.id.profile_button);
        Intent alarmLink = new Intent(this, AlarmActivity.class);
        alarmLink.putExtra("user", currentUser);
        Intent medicineLink = new Intent(this, MedicineActivity.class);
        medicineLink.putExtra("user", currentUser);
        Intent profileLink = new Intent(this, ProfileActivity.class);
        profileLink.putExtra("user", currentUser);

        alarmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(alarmLink);
            }
        });
        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(medicineLink);
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(profileLink);
            }
        });

        RecyclerView alarmRecyclerView = findViewById(R.id.alarmRecyclerView);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        alarms = new ArrayList<>();
        loadAlarmsFromCsv();

        alarmList = new AlarmList(alarms);
        alarmRecyclerView.setAdapter(alarmList);

    }

    /**
     * Requests location permissions from Android
     */
    private void requestLocationPermissions() {
        //Request location permissions if application does not possess them
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    /**
     * Requests location from Android
     */
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

    /**
     * Updates the weather data variables
     */
    private void updateWeatherData() {
        if(service != null){
            try {
                service.setLocation(latitude, longitude);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherData();
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Initializes the weather service
     */
    private void initWeatherService(){
        try{
            service = new WeatherService(latitude, longitude, this);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateWeatherData();
                }
            }, INIT_DELAY_TIME);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Updates the weather data displays
     */
    private void showWeatherData(){
        if(service != null){
            tempDisplay.setText(Double.toString(service.getTemperature()));
            weatherDisplay.setText(service.getWeather());
            windDisplay.setText(service.getWindType());
        }
    }

    /**
     * Loads the user's alarms from a CSV
     */
    private void loadAlarmsFromCsv() {
        alarms.clear();
        try (FileInputStream fis = openFileInput("alarms.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    alarms.add(new Alarm(parts[0], parts[1], parts[2].toUpperCase()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load alarms from CSV", Toast.LENGTH_LONG).show();
        }
    }
}