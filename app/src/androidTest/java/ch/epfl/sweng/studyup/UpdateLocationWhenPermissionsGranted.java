package ch.epfl.sweng.studyup;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;


@RunWith(AndroidJUnit4.class)
public class UpdateLocationWhenPermissionsGranted {
    private static final double MOCK_LAT = 45.6;
    private static final double MOCK_LONG = 30.1;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void updateMarker() {
        Log.d("GPS_MAP", "Starting test updateLocation");
        android.location.Location location = new android.location.Location("T");
        location.setLatitude(MOCK_LAT);
        location.setLongitude(MOCK_LONG);
        Utils.mockLoc = new Location(location);
        Utils.isMockEnabled = true;
        mActivityRule2.launchActivity(new Intent());

        assertEquals(MOCK_LAT, Utils.position.latitude);
        assertEquals(MOCK_LONG, Utils.position.longitude);
    }

}
