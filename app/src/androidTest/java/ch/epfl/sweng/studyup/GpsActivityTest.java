package ch.epfl.sweng.studyup;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class GpsActivityTest {
    private double latitude = 10.0;
    private double longitude = 20.0;
    private boolean mockEnabled = true;
    private LocationManager lm = null;
    private LocationTracker locationTracker = null;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        lm = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationTracker = new LocationTracker(mActivityRule.getActivity());
        assertNotNull(locationTracker);
        assertNotNull(lm);
        if (lm.getAllProviders().contains("test")) {
            lm.removeTestProvider("test");
        }
        try {
            lm.addTestProvider("test", false, false, false, false, false, false, false, Criteria.POWER_HIGH, Criteria.ACCURACY_HIGH);
            lm.setTestProviderEnabled("test", true);

            Location location = new Location("test");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAccuracy(1);
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            location.setTime(System.currentTimeMillis());
            lm.setTestProviderLocation("test", location);

            Log.d("Mock location enabled", "GPS_TEST");
        }catch (SecurityException e){
            mockEnabled = false;
            Log.d("Mock location disabled", "GPS_TEST");
        }
    }

    @Test
    public void GpsActivityTest(){
        if(mockEnabled) {
            onView(withId(R.id.locButton)).perform(click());
            onView(withId(R.id.helloWorld)).check(matches(withText("Latitude: " + latitude + "\nLongitude: " + longitude)));
        }
    }

    @Test
    public void getLastLocationTest(){
        if(mockEnabled){
            Location loc = locationTracker.getLastLocation(lm, "test");
            assertNotNull(loc);
            assertEquals(latitude, loc.getLatitude(), 10e-4);
            assertEquals(longitude, loc.getLongitude(), 10e-4);
        }else{
            Location loc = locationTracker.getLastLocation(lm, LocationManager.GPS_PROVIDER);

            Location locGps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(locGps != null){
                assertNotNull(loc);
                assertEquals(locGps.getLatitude(), loc.getLatitude(), 10e-4);
                assertEquals(locGps.getLongitude(), loc.getLongitude(), 10e-4);
            }else{
                assertEquals(loc, null);
            }
        }
    }

    @Test
    public void getProviderTest(){
        if(mockEnabled){
            assertEquals("test", locationTracker.getProvider(lm));
        }else{
            assertEquals(LocationManager.GPS_PROVIDER, locationTracker.getProvider(lm));
        }
    }

    @After
    public void removeTestProvider(){
        if(lm.getAllProviders().contains("test")){
            lm.removeTestProvider("test");
        }
    }
}
