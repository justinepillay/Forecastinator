package com.example.forecastinator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnCitySearch;
    Button btnCurrentLocation;
    EditText edCityName;
    RecyclerView rcForecasts;
    TextView tvCityName;

    String messageToShare= "";

    ForecastAdapter fAdapter;
    RecyclerView.LayoutManager fLayoutManager;
    private ImageLoader imageLoader;
    int LOCATION_REQUEST_CODE= 10001;
    private static final String TAG = "CurrentLocationActivity";
    FusedLocationProviderClient fusedLocationProviderClient ;

    JSONParse jsonParse;
    ArrayList<ForecastItem> forecastItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnCitySearch = findViewById(R.id.btnCitySearch);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        edCityName = findViewById(R.id.edCityName);
        rcForecasts = findViewById(R.id.rvForecasts);
        tvCityName = findViewById(R.id.tvCityName);
        rcForecasts.setHasFixedSize(true);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();

        fLayoutManager = new LinearLayoutManager(MainActivity.this);
        rcForecasts.setLayoutManager(fLayoutManager);


            getDurbanForecastsFive();


        btnCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = edCityName.getText().toString();
                if (!cityName.equals("")) {
                    // do the thing
                    getCitySearchForecasts(cityName);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city name.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                edCityName.setText("");
                
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getLastLocation();
                }
                else
                {
                  askLocationPermission();
                }
            }
        });


    }


    public void getDurbanForecastsFive()
    {

        URL durbanURL = URLBuilder.getForecastURL("305605");
        //Log.i("DURBAN_URL", durbanURL.toString());
        setForecastValues(durbanURL.toString(), "Durban");

    }

    public void getCitySearchForecasts(String cityName)
    {
        URL citySearchURL = URLBuilder.citySearchUrl(cityName);
      //  Log.i("CITY_SEARCH_URL", citySearchURL.toString());

        getCityData(citySearchURL.toString());

    }

    public void setForecastValues(String JSON_URL, final String name)
    {

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            jsonParse = new JSONParse();

                            forecastItems = jsonParse.getForecastList(response, name);
                            DataHolder.setList(forecastItems);


                            fAdapter = new ForecastAdapter(forecastItems);
                            rcForecasts.setAdapter(fAdapter);
                            fAdapter.setOnItemCLickListener(new ForecastAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(int position)
                                {
                                    displayForecastDetails(position);
                                }
                            });


                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //displaying the error in toast if occurrs
                        error.printStackTrace();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void getCityData(String JSON_URL)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                            jsonParse = new JSONParse();

                            String cityId = jsonParse.getFirstCityId(response);

                           URL forecastURL = URLBuilder.getForecastURL(cityId);
                         //  Log.i("FORECAST_URL", forecastURL.toString());
                        String name = jsonParse.getFirstCityName(response);
                        tvCityName.setText(name);
                           setForecastValues(forecastURL.toString(), name);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //displaying the error in toast if occurrs
                        error.printStackTrace();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void getLastLocation()
    {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if (location != null)
                {

                    String latitude = location.getLatitude()+"";
                    String longitude = ""+location.getLongitude();

                    URL locationsURL = URLBuilder.getCityFromLocationURL(latitude+","+longitude);

                   // Log.i("LOCATION_URL", locationsURL.toString());

                    getCityData(locationsURL.toString());



                } else {
                    Log.e(TAG, "onSuccess: Location was null");
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "on failure: " + e.getLocalizedMessage());
            }
        });
    }

    public void askLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                Log.d(TAG, "askLocationPermission: you should show alert dialog...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode== LOCATION_REQUEST_CODE)
        {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission granted
                getLastLocation();
            }
            {
                //Permission not granted

            }
        }
    }

    public void displayForecastDetails(int position)
    {

        ArrayList<ForecastItem> forecasts = DataHolder.getList();
        if(forecasts.size()>=1)
        {

            AlertDialog.Builder alert = new AlertDialog.Builder((this));
            messageToShare = "FORECAST DETAILS\nCITY:   "
                    +forecasts.get(position).getfCity()+
                    "\nDATE:   "+forecasts.get(position).getfDate()
                    +"\n\n" +
                    ""+forecasts.get(position).getfStatus()+"\n"+
                    "MAX:    "+ forecasts.get(position).getfMaxTemp()+""
                    +"\nMIN:    " +forecasts.get(position).getfMinTemp();

            alert.setMessage(messageToShare);

            alert.setNegativeButton("Okay", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Toast.makeText(MainActivity.this, "Soon...", Toast.LENGTH_SHORT).show();
                }
            });

            alert.setPositiveButton("Share", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    sendDataAsString();
                }
            });

            alert.show();

        }

    }

    public void sendDataAsString()
    {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, messageToShare);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Forecast Information");
            startActivity(shareIntent);

    }





}