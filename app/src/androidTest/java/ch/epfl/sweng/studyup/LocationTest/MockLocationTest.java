package ch.epfl.sweng.studyup.LocationTest;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.TestbedActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static junit.framework.TestCase.assertEquals;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

@RunWith(AndroidJUnit4.class)
public class MockLocationTest {
    private static final double MOCK_LAT = 43.6;
    private static final double MOCK_LONG = 47.9;
    private boolean setMockLocation = false;

    @Rule
    public final ActivityTestRule<TestbedActivity> mActivityRule2 =
            new ActivityTestRule<>(TestbedActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @BeforeClass
    public static void runOnceBeforeClass() {
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }

    @Before
    public void SetupMockLocationTest() {
        android.location.Location location = new android.location.Location("Mock");
        location.setLatitude(MOCK_LAT);
        location.setLongitude(MOCK_LONG);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());

        MOC_LOC = new Location(location);
        MOCK_ENABLED = true;
        POSITION = null;
        mActivityRule2.launchActivity(new Intent());
    }

    @Test
    public void backgroundLocationTest() {
        Log.i("GPS_TEST", "Started test");
        setupMock();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("GPS_TEST", e.getMessage());
        }

        if (setMockLocation) {
            mActivityRule2.getActivity().scheduleBackgroundLocation();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e("GPS_TEST", e.getMessage());
            }

            Log.i("GPS_TEST", "Started assert");
            assertEquals(MOCK_LAT, POSITION.latitude);
            assertEquals(MOCK_LONG, POSITION.longitude);
        }
    }

    public void setupMock(){
        LOCATION_PROVIDER_CLIENT.setMockMode(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("GPS_TEST", "Mock mode set");
                LOCATION_PROVIDER_CLIENT.setMockLocation(MOC_LOC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("GPS_TEST", "Mock location set");
                        setMockLocation = true;
                    }
                });
            }
        });
    }

    @After
    public void cleanUp() {
        POSITION = null;
        MOC_LOC = null;
        MOCK_ENABLED = false;
    }
}