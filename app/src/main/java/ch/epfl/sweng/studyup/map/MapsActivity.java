package ch.epfl.sweng.studyup.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.LOCATION_REQ_FASTEST_INTERVAL;
import static ch.epfl.sweng.studyup.utils.Constants.LOCATION_REQ_INTERVAL;
import static ch.epfl.sweng.studyup.utils.Constants.MAP_INDEX;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.ROOM_OBJECTIVE;

/**
 * MapActivity
 * <p>
 * Code used in the activity_map.
 */

public class MapsActivity extends NavigationStudent implements OnMapReadyCallback {
    private Marker location = null;
    private Marker roomObjective = null;
    private GoogleMap mMap = null;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private boolean isFirstPosition = true;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQ_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQ_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d("GPS_MAP", "Created map activity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        navigationSwitcher(MapsActivity.this, MapsActivity.class, MAP_INDEX);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("GPS_MAP", "Map ready position = " + POSITION);
        onLocationUpdate(POSITION);
        findAndMarkRoom(Player.get().getCurrentCourse() != null ?
                Player.get().getCurrentCourse().getRoom() : null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("GPS_MAP", "Pause map");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                    Looper.myLooper());
            Log.d("GPS_MAP", "Request location updates map");
        }
        Log.d("GPS_MAP", "Resume map");
    }

    public void onLocationUpdate(LatLng latLong) {
        if (latLong != null) {
            Log.d("GPS_MAP", "New position map: " + latLong.toString());
            if (mMap != null) {
                if (location != null) {
                    location.remove();
                }
                location = mMap.addMarker(new MarkerOptions()
                        .position(latLong)
                        .title(getString(R.string.title_playerposition))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                if(isFirstPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 18));
                    isFirstPosition = false;
                }
            }
            POSITION = new LatLng(latLong.latitude, latLong.longitude);
        }
    }

    public void findAndMarkRoom(Room room) {
        if(mMap != null) {
            if(room != null) {
                Log.d("GPS_MAP", "New objective: " + room.getLocation().toString());
                roomObjective = mMap.addMarker(new MarkerOptions()
                        .position(room.getLocation())
                        .title(getString(R.string.room_objective))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
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
        if (location != null) {
            return new LatLng(location.getPosition().latitude, location.getPosition().longitude);
        } else {
            return null;
        }
    }
}


