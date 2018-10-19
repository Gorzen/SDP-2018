package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ch.epfl.sweng.studyup.utils.Utils;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class UpdateLocationWhenPermissionsGranted {
    private static final double MOCK_LAT = 10;
    private static final double MOCK_LONG = 0;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void grantMockLocation(){
        Log.d("GPS_MAP", "Starting test updateLocation");
        Log.d("GPS_MAP", "Activity: " + mActivityRule2.getActivity());
        android.location.Location location = new android.location.Location("Mock");
        location.setLatitude(MOCK_LAT);
        location.setLongitude(MOCK_LONG);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());
        Utils.mockLoc = new Location(location);
        Utils.isMockEnabled = true;
        mActivityRule2.launchActivity(new Intent());
        /*
        try{
            Runtime.getRuntime().exec(String.format("adb shell appops set %s android:mock_location allow", mActivityRule2.getActivity().getApplicationContext().getPackageName()));
        }catch(IOException e){
            Log.e("GPS_TEST", e.getMessage());
        }
        */
    }

    @Test
    public void updateLocation() {
        Utils.locationProviderClient.setMockMode(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GPS_MAP", "Set mock mode was successful" + Utils.mockLoc);
                Utils.locationProviderClient.setMockLocation(Utils.mockLoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("GPS_MAP", "Mock location set");

                        Utils.locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.d("GPS_MAP", "Last location: " + location);
                                Utils.position = new LatLng(location.getLatitude(), location.getLongitude());
                                Log.d("GPS_MAP", "Got last location");

                                Log.d("GPS_MAP", "Started assert");
                                assertEquals(MOCK_LAT, Utils.position.latitude);
                                assertEquals(MOCK_LONG, Utils.position.longitude);
                            }
                        });
                    }
                });
            }
        });
    }

}
