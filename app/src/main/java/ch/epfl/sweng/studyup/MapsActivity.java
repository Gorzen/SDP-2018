package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Marker location;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult != null) {
                Location lastLocation = locationResult.getLastLocation();
                onLocationUpdate(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d("GPS_MAP", "Created map activity");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("GPS_MAP", "Map ready position = " + Utils.position);
        onLocationUpdate(Utils.position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("GPS_MAP", "Pause map");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
        Log.d("GPS_MAP", "Resume map");
    }

    public void onLocationUpdate(LatLng latLong) {
        if(mMap != null){
            if(location != null){
                location.remove();
            }
            if(latLong != null) {
                Log.d("GPS_MAP", "New position map: " + latLong);
                location = mMap.addMarker(new MarkerOptions().position(latLong).title("Player position"));
                Utils.position = new LatLng(latLong.latitude, latLong.longitude);
            }
        }
    }

    public long getIntervals() {
        return locationRequest.getInterval();
    }

    public long getFastedIntervals() {
        return locationRequest.getFastestInterval();
    }

    public int getPriority() {
        return locationRequest.getPriority();
    }

    public LatLng getMarkerPos() {
        return new LatLng(location.getPosition().latitude, location.getPosition().longitude);
    }
}
