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
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
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
    };
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
        if(ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this.context, "Please grant position permission.", Toast.LENGTH_LONG).show();
            return null;
        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(this.context.LOCATION_SERVICE);
            String provider = getProvider(locationManager);
            if (locationManager.isProviderEnabled(provider)) {      //TODO may invoke NullPointerException
                locationManager.requestLocationUpdates(provider, 1000, 10, listener);
                Location location = getLastLocation(locationManager, provider);
                if(location == null){
                    Toast.makeText(this.context, "Unable to retrieve GPS position.", Toast.LENGTH_LONG).show();
                }
                return location;
            } else {
                Toast.makeText(this.context, "Please enable GPS.", Toast.LENGTH_LONG).show();
                return null;
            }
        }
    }
    public Location getLastLocation(LocationManager locationManager, String provider){
        if(ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        }else {
            Location currLoc = locationManager.getLastKnownLocation(provider);
            return currLoc;
        }
    }
}
