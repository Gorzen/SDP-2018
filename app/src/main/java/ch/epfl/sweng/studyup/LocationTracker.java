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

public class LocationTracker {

    private Context context;

    public LocationTracker(Context context) {
        this.context = context;
    }

    public String getProvider(LocationManager lm){
        if(lm.getAllProviders().contains("test")){
            return "test";
        }else{
            return LocationManager.GPS_PROVIDER;
        }
    }

    public Location getLocation() {
        String message = "";
        Location retValue = null;

        if(ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            message = "Please grant position permission.";
        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(this.context.LOCATION_SERVICE);
            String provider = getProvider(locationManager);

            if (locationManager.isProviderEnabled(provider)) {
                locationManager.requestLocationUpdates(provider, 1000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {}
                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {}
                    @Override
                    public void onProviderEnabled(String s) {}
                    @Override
                    public void onProviderDisabled(String s) {}});

                Location currLoc = locationManager.getLastKnownLocation(provider);

                if (currLoc == null) {
                    message = "Unable to retrieve GPS position.";
                }

                retValue = currLoc;
            } else {
                message = "Please enable GPS.";
            }
        }

        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();

        return retValue;
    }
}
