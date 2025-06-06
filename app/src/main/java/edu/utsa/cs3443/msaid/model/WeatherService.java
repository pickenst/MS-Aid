package edu.utsa.cs3443.msaid.model;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

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

/**
 * @author Trey Pickens
 * The WeatherService class is MS Aids weather service. It pulls live data from the GeoWeather API and produces weather data.
 */
public class WeatherService {

    private final String CLOUDY_ICON = "cloudy";
    private final String COLD_ICON = "cold";
    private final String HOT_ICON = "hot";
    private final String HAIL_ICON = "hail";
    private final String PARTLY_CLOUDY_ICON = "partly_cloudy";
    private final String RAIN_ICON = "rain";
    private final String SNOW_ICON = "snow";
    private final String SUNNY_ICON = "sunny";
    private final String THUNDERSTORM_ICON = "thunderstorm";

    private double longitude;
    private double latitude;
    private HashMap<Integer,String> weatherCodeTable;

    private String windType;
    private double windSpeed;
    private double temperature;
    private String weather;
    private int weatherCode;

    private Drawable weatherIcon = null;
    private Drawable tempIcon = null;
    private final Activity ACTIVE_ACTIVITY;

    /**
     * Initializes the Weather Service
     * @param latitude the user's current latitude (double)
     * @param longitude the user's current longitude (double)
     * @param activity the Activity the weather service is used in (Activity)
     * @throws JSONException
     * @throws IOException
     */
    public WeatherService(double latitude, double longitude, Activity activity) throws JSONException, IOException {

        this.weatherCodeTable = new HashMap<>();
        this.initCodeTable(activity);
        this.setLocation(latitude, longitude);
        this.ACTIVE_ACTIVITY = activity;
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

    /**
     * The primary function of the service which sets it's local weather variables
     * @throws IOException
     * @throws JSONException
     */
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

        this.weatherCode = weatherCode;
        System.out.print(weatherCode);
        this.weather = weatherCodeTable.get(weatherCode);
        this.windSpeed = weatherInfo.optDouble("wind_speed_10m");
        this.windType = getWindType(windSpeed);
        this.temperature = weatherInfo.optDouble("temperature_2m");
    }

    /**
     * Makes a data request towards the API
     * @param urlString the URL of the API
     * @return HttpURLConnection
     */
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

    /**
     * Reads the requested data from the API
     * @param apiConnection the requested data
     * @return String
     * @throws IOException
     */
    private String readApiResponse(HttpURLConnection apiConnection) throws IOException{
        StringBuilder resultJson = new StringBuilder();

        Scanner scnr = new Scanner(apiConnection.getInputStream());

        while(scnr.hasNext()){
            resultJson.append(scnr.nextLine());
        }

        scnr.close();

        return resultJson.toString();
    }

    /**
     * Initializes the WMOCodes from the csv file to the 'Code-to-Meaning' HashMap
     * @param activity the current Activity of the weather service
     * @throws FileNotFoundException
     */
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

    /**
     * Gets the typing of the wind based on speed
     * @param windSpeed the user's local windspeed in MPH (double)
     * @return String
     */
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

    /**
     * Gets the current weather type
     * @return String
     */
    public String getWeather(){
        return this.weather;
    }

    /**
     * Gets the current wind type
     * @return String
     */
    public String getWindType(){
        return this.windType;
    }

    /**
     * Gets the current temperature
     * @return double
     */
    public double getTemperature(){
        return this.temperature;
    }

    /**
     * Gets the current wind speed
     * @return double
     */
    public double getWindSpeed(){
        return this.windSpeed;
    }

    public void setWeatherIcon() {
        String assetName = null;
        if(this.weatherCode <= 1){
            assetName = SUNNY_ICON;
        }
        else if(this.weatherCode == 2){
            assetName = PARTLY_CLOUDY_ICON;
        }
        else if(this.weatherCode <= 48){
            assetName = CLOUDY_ICON;
        }
        else if(this.weatherCode <= 67){
            assetName = RAIN_ICON;
        }
        else if(this.weatherCode <= 77){
            assetName = SNOW_ICON;
        }
        else if(this.weatherCode <= 82){
            assetName = RAIN_ICON;
        }
        else if(this.weatherCode <= 86){
            assetName = SNOW_ICON;
        }
        else{
            assetName = THUNDERSTORM_ICON;
        }
        //String assetName = this.weather.toLowerCase();
        int id = ACTIVE_ACTIVITY.getResources().getIdentifier(assetName, "drawable", this.ACTIVE_ACTIVITY.getPackageName());
        this.weatherIcon = AppCompatResources.getDrawable(ACTIVE_ACTIVITY, id);
    }

    public Drawable getWeatherIcon() {
        return weatherIcon;
    }

    public void setTempIcon(){
        String assetName = null;
        if(this.temperature <= 65){
            assetName = COLD_ICON;
        }
        else if(temperature >= 80){
            assetName = HOT_ICON;
        }
        if(assetName == null){
            return;
        }
        int id = ACTIVE_ACTIVITY.getResources().getIdentifier(assetName, "drawable", this.ACTIVE_ACTIVITY.getPackageName());
        this.tempIcon = AppCompatResources.getDrawable(ACTIVE_ACTIVITY, id);
    }

    public Drawable getTempIcon(){
        return this.tempIcon;
    }

    public int getWeatherCode(){
        return this.weatherCode;
    }
}
