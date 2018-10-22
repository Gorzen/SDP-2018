package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

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
        Utils.locationProviderClient.flushLocations();
        Utils.locationProviderClient.setMockMode(false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e("GPS_TEST", "Interrupted setupMockLocationTest");
        }
    }


    @Test
    public void backgroundLocationTest(){
        Log.i("GPS_TEST", "Started test");
        Utils.locationProviderClient.setMockMode(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("GPS_TEST", "Mock mode set");
                Utils.locationProviderClient.setMockLocation(Utils.mockLoc);
            }
        });
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            Log.e("GPS_TEST", e.getMessage());
        }
        mActivityRule2.getActivity().scheduleBackgroundLocation();

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            Log.e("GPS_TEST", e.getMessage());
        }

        if(Utils.position != null) {
            Log.i("GPS_TEST", "Started assert");
            assertEquals(MOCK_LAT, Utils.position.latitude);
            assertEquals(MOCK_LONG, Utils.position.longitude);
        }
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
