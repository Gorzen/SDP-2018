package ch.epfl.sweng.studyup.NPCTest;

import android.location.Location;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kosalgeek.android.caching.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NPCTest {
    Location location;
    private BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);
    private OnSuccessListener<Location> onSuccessListener = getLocation.onSuccessListener;
    private final String name = "Coco";
    private final int image = 32913;
    private final NPC npc = new NPC(name, null, image);


    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule2 =
            new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void ifWithinRangeOfNPCTest() {
        setLocation(Constants.CYNTHIA_POSITION);
        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                UiObject button = uiDevice.findObject(new UiSelector().text(GlobalAccessVariables.MOST_RECENT_ACTIVITY.getString(R.string.NPC_accept)));
                try {
                    if (button.exists() && button.isEnabled()) {
                        button.click();
                    }
                } catch (Exception e) {
                    assertFalse(true);
                }
            }

        });
        Utils.waitAndTag(5000, "");
    }

    @Test
    public void getNameTest() {
        assertTrue(npc.getName().equals(name));
    }

    @Test
    public void getImageTest() {
        assertEquals(image, npc.getImage());
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
