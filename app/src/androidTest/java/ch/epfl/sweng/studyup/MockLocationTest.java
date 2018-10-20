package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ch.epfl.sweng.studyup.utils.Utils;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class MockLocationTest {
    private static final double MOCK_LAT = 10;
    private static final double MOCK_LONG = 0;
    private boolean mockLocationSet = false;
    private boolean jobFinished = false;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void SetupMockLocationTest() {
        Log.d("GPS_MAP", "Starting test updateLocation");
        android.location.Location location = new android.location.Location("Mock");
        location.setLatitude(MOCK_LAT);
        location.setLongitude(MOCK_LONG);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());
        Utils.mockLoc = new Location(location);
        Utils.isMockEnabled = true;
        Utils.position = null;
        mActivityRule2.launchActivity(new Intent());
    }

    @Test
    public void backgroundLocationTest() {
        Utils.locationProviderClient.setMockMode(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GPS_MAP", "Set mock mode was successful" + Utils.mockLoc);
                Utils.locationProviderClient.setMockLocation(Utils.mockLoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("GPS_MAP", "Mock location set");
                        mockLocationSet = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assertEquals(3, 4);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(5, 6);
            }
        });

        //Wait for Mock Mode and Mock Location to finish
        while(!mockLocationSet);

        assertEquals(1, 2);

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Log.e("GPS_TEST", e.getMessage());
        }

        /*
        Log.d("GPS_MAP", "Schedule background location");
        mActivityRule2.getActivity().scheduleBackgroundLocation();
        */

        Utils.locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Utils.position = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });


        //Wait for Async Task to finish
        while (!jobFinished){
            jobFinished = !(Utils.position == null);
        }

        Log.d("GPS_MAP", "Started assert");
        assertEquals(MOCK_LAT, Utils.position.latitude);
        assertEquals(MOCK_LONG, Utils.position.longitude);
    }
}
