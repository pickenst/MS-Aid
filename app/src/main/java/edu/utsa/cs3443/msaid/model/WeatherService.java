package edu.utsa.cs3443.msaid.model;

import android.app.Activity;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class WeatherService {
    /*
        TODO: _HEAVY_ Cleanup
     */
    private double longitude;
    private double latitude;
    private HashMap<Integer,String> weatherCodeTable;

    private String windType;
    private double windSpeed;
    private double temperature;
    private String weather;


    public WeatherService(double latitude, double longitude, Activity activity) throws JSONException, IOException {

        weatherCodeTable = new HashMap<>();
        initCodeTable(activity);
        this.setLocation(latitude, longitude);
    }

    public void setLongitude(double longitude) throws JSONException, IOException {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) throws JSONException, IOException {
        this.latitude = latitude;
    }

    public void setLocation(double latitude, double longitude) throws JSONException, IOException {
        this.latitude = latitude;
        this.longitude = longitude;
        fetchData();
    }

    private void fetchData(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->{
            try {
                setWeatherData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setWeatherData() throws IOException, JSONException {
        String geoWeatherURL = "https://api.open-meteo.com/v1/forecast?latitude=" + this.latitude + "&longitude=" + this.longitude + "&current=temperature_2m,weather_code,wind_speed_10m&temperature_unit=fahrenheit&wind_speed_unit=mph";

        HttpURLConnection apiConnection = fetchApiResponse(geoWeatherURL);
        try {
            if(apiConnection.getResponseCode() != 200){
                System.out.println("Error: Api not loaded");
            }
        } catch (IOException e) {
            System.out.println("Failed to connect to weather API");
            return;
        }

        String JSONResponse = readApiResponse(apiConnection);

        JSONObject resultsJsonObj = null;
        try{
            resultsJsonObj = new JSONObject(JSONResponse);
        } catch (JSONException e) {
            System.out.println("Failed to init resultsJSONObj");
        }

        JSONObject weatherInfo = resultsJsonObj.getJSONObject("current");
        int weatherCode = weatherInfo.optInt("weather_code");

        this.weather = weatherCodeTable.get(weatherCode);
        this.windSpeed = weatherInfo.optDouble("wind_speed_10m");
        this.windType = getWindType(windSpeed);
        this.temperature = weatherInfo.optDouble("temperature_2m");
    }

    private HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("GET");

            return conn;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private String readApiResponse(HttpURLConnection apiConnection) throws IOException{
        StringBuilder resultJson = new StringBuilder();

        Scanner scnr = new Scanner(apiConnection.getInputStream());

        while(scnr.hasNext()){
            resultJson.append(scnr.nextLine());
        }

        scnr.close();

        return resultJson.toString();
    }

    private void initCodeTable(Activity activity) throws FileNotFoundException {
        String fileName = "WMOCodes.csv";
        AssetManager manager = activity.getAssets();
        Scanner scnr = null;
        InputStream WMOSheet = null;
        try {
            WMOSheet = manager.open(fileName);
            scnr = new Scanner(WMOSheet);
        } catch (IOException e) {
            System.out.println("Failed to open WMOCodes.csv");
        }
        while (scnr.hasNextLine()){
            String[] buffer = scnr.nextLine().split(",");
            weatherCodeTable.put(Integer.parseInt(buffer[0]), buffer[1]);
        }
    }

    private String getWindType(double windSpeed){
        if(windSpeed <= 3){
            return "Little Wind";
        }
        if(windSpeed <= 12){
            return "Light Breeze";
        }
        if(windSpeed <= 18){
            return "Moderate Breeze";
        }
        if(windSpeed <= 31){
            return "Strong Breeze";
        }
        if(windSpeed <= 54){
            return "Gale Wind";
        }
        if(windSpeed <= 63){
            return "Strong Gale";
        }
        else{
            return "Storm Winds";
        }
    }
    public String getWeather(){
        return this.weather;
    }
    public String getWindType(){
        return this.windType;
    }
    public double getTemperature(){
        return this.temperature;
    }
    public double getWindSpeed(){
        return this.windSpeed;
    }


}
