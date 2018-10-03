package ch.epfl.sweng.studyup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Location extends AppCompatActivity {
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstance) {
        client = LocationServices.getFusedLocationProviderClient(this);

        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if(location != null) {

                }
            }
        });
    }
}
