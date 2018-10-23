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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.utils.Utils;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class MockLocationTest {
    private static final double MOCK_LAT = 43.6;
    private static final double MOCK_LONG = 47.9;
    private boolean setMockMode = false;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void SetupMockLocationTest() {
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
        Log.i("GPS_TEST", "Started test");
        Utils.locationProviderClient.setMockMode(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isCanceled()){
                    Log.i("GPS_TEST", "Mock mode set");
                    setMockMode = true;
                    Utils.locationProviderClient.setMockLocation(Utils.mockLoc);
                }
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("GPS_TEST", e.getMessage());
        }

        if (setMockMode) {
            mActivityRule2.getActivity().scheduleBackgroundLocation();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e("GPS_TEST", e.getMessage());
            }

            Log.i("GPS_TEST", "Started assert");
            assertEquals(MOCK_LAT, Utils.position.latitude);
            assertEquals(MOCK_LONG, Utils.position.longitude);
        }
    }

    @After
    public void cleanUp() {
        Utils.position = null;
        Utils.mockLoc = null;
        Utils.isMockEnabled = false;
    }
}
