package com.example.mycontactlist;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;

public class ContactMapActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.radioGroupSortBy), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGetLocationButton();
    }
    private void initGetLocationButton() {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

                    gpsListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                            TextView txtLongitude = (TextView) findViewById(R.id.textLongtitude);
                            TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);

                            txtLatitude.setText(String.valueOf(location.getLatitude()));
                            txtLongitude.setText(String.valueOf(location.getLongitude()));
                            txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                        public void onProviderEnabled(String provider) {}
                        public void onProviderDisabled(String provider) {}
                    };

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
                }
                catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startLocationUpdates(){if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
    }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

            gpsListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = findViewById(R.id.textLatitude);
                    TextView txtLongitude = findViewById(R.id.textLongtitude);
                    TextView txtAccuracy = findViewById(R.id.textAccuracy);

                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }
}