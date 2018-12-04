package ch.epfl.sweng.studyup.NPCTest;

import android.location.Location;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kosalgeek.android.caching.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class NPCTest {
    Location location;
    private BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);
    private OnSuccessListener<Location> onSuccessListener = getLocation.onSuccessListener;

    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule2 =
            new ActivityTestRule<>(InventoryActivity.class);

    @Test
    public void ifWithinRangeOfNPCTest() {
        //setLocation(Constants.CYNTHIA_POSITION);
        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                onView(withText(GlobalAccessVariables.MOST_RECENT_ACTIVITY.getString(R.string.NPC_accept))).perform(click());
            }
        });
    }
    private void setLocation(LatLng playerPosition) {
        location = new Location("Mock");
        location.setLatitude(playerPosition.latitude);
        location.setLongitude(playerPosition.longitude);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());
    }
}
