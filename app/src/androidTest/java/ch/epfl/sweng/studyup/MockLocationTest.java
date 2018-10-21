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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.After;
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
    private static final double MOCK_LAT = 43.6;
    private static final double MOCK_LONG = 47.9;
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
        mockLocationSet = false;
        jobFinished = false;

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
        Utils.locationProviderClient.flushLocations().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GPS_MAP", "Flushed locations");
            }
        });
        Utils.locationProviderClient.setMockMode(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GPS_MAP", "Mock mode false");
            }
        });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void backgroundLocationTest() {
        Log.d("GPS_MAP", "Started run");
        Utils.locationProviderClient.setMockMode(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("GPS_MAP", "Set mock mode was successful");
                Utils.locationProviderClient.setMockLocation(Utils.mockLoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("GPS_MAP", "Mock location set: " + Utils.mockLoc);
                        mockLocationSet = true;
                        Log.d("GPS_MAP", "mockLocationSet: " + mockLocationSet);
                        //assertEquals(true, mockLocationSet);
                        //assertEquals(10, 11);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assertEquals(3, 4);
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        assertEquals(300, 301);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(5, 6);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("GPS_MAP", "Complete");
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                assertEquals(200, 201);
            }
        });

        //Wait for Mock Mode and Mock Location to finish
        while (!mockLocationSet) {
            Log.d("GPS_MAP", "Start while " + mockLocationSet);
        }

        //assertEquals(1, 2);

        Log.d("GPS_MAP", "Change Pos 2");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Log.e("GPS_TEST", e.getMessage());
        }

        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("GPS_MAP", "Schedule background location");
                mActivityRule2.getActivity().scheduleBackgroundLocation();
            }
        });
        /*
        Log.d("GPS_MAP", "Schedule background location");
        mActivityRule2.getActivity().scheduleBackgroundLocation();
        */


        //Wait for Async Task to finish
        while (!jobFinished) {
            jobFinished = !(Utils.position == null);
        }

        Log.d("GPS_MAP", "Started assert " + mockLocationSet + " " + jobFinished);
        assertEquals(MOCK_LAT, Utils.position.latitude);
        assertEquals(MOCK_LONG, Utils.position.longitude);
    }

    @After
    public void cleanUp() {
        Utils.locationProviderClient.flushLocations();
        Utils.position = null;
        Utils.mockLoc = null;
        Utils.isMockEnabled = false;
        Utils.locationProviderClient = null;
    }
}
