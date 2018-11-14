package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.map.MapsActivity;

import static ch.epfl.sweng.studyup.utils.Constants.LOCATION_REQ_FASTEST_INTERVAL;
import static ch.epfl.sweng.studyup.utils.Constants.LOCATION_REQ_INTERVAL;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapsActivityTest {
    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(MapsActivity.class);
    private final double LAT = 35.323;
    private final double LONG = 56.43;
    private LatLng latlng = new LatLng(LAT, LONG);

    @BeforeClass
    public static void runOnceBeforeClass() {
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }

    @Test
    public void locationRequestSetsUpCorrectly() {
        assertEquals(LOCATION_REQ_INTERVAL, mActivityRule.getActivity().getIntervals());
        assertEquals(LOCATION_REQ_FASTEST_INTERVAL, mActivityRule.getActivity().getFastedIntervals());
        assertEquals(LocationRequest.PRIORITY_HIGH_ACCURACY, mActivityRule.getActivity().getPriority());
    }

    @Test
    public void onLocationUpdateChangesUtilsPos() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().onLocationUpdate(latlng);
                assertEquals(LAT, POSITION.latitude, 0.0);
                assertEquals(LONG, POSITION.longitude, 0.0);
            }
        });
    }

    @Test
    public void getMarkerPosNoCrashWithBadParams() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().getMarkerPos();
                mActivityRule.getActivity().onMapReady(null);
                mActivityRule.getActivity().getMarkerPos();
            }
        });
    }

    /*@Test
    public void onMapReadyNoCrashWithBadParams() {
        mActivityRule.getActivity().onMapReady(null);
    }

    @Test
    public void AtestOptionNoException() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
    */
}
