package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class LocationTracker implements LocationListener {

    private Context context;

    public LocationTracker(Context context) {
        this.context = context;
    }

    public Location getLocation() {

        if (ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this.context, "Permission denied for location access.", Toast.LENGTH_LONG).show();
            return null;
        }

        LocationManager locationManager = (LocationManager)context.getSystemService(this.context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
            Location currLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return currLoc;
        }
        else {
            Toast.makeText(this.context, "Please enable GPS.", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
