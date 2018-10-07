package ch.epfl.sweng.studyup;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testGPS(){
        double latitude = 10.0;
        double longitude = 20.0;

        LocationManager lm = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(lm.getAllProviders().contains("test")){
            lm.removeTestProvider("test");
        }

        lm.addTestProvider("test", false, false, false, false, false, false, false, Criteria.POWER_HIGH, Criteria.ACCURACY_HIGH);
        lm.setTestProviderEnabled("test", true);

        Location location = new Location("test");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());
        lm.setTestProviderLocation("test", location);

        Looper.prepare();

        LocationTracker locationTracker = new LocationTracker(mActivityRule.getActivity().getApplicationContext());
        Location currLoc = locationTracker.getLocation();

        assertNotNull(currLoc);
        assertEquals(latitude, currLoc.getLatitude(), 10e-4);
        assertEquals(longitude, currLoc.getLongitude(), 10e-4);

        onView(withId(R.id.locButton)).perform(click());

        onView(withId(R.id.helloWorld)).check(matches(withText("Latitude: " + latitude + "\nLongitude: " + longitude)));

        lm.removeTestProvider("test");
    }
}
