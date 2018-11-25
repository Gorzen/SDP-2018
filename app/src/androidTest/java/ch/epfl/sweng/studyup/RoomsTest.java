package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.TestbedActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RoomsTest {


    @Rule
    public final ActivityTestRule<TestbedActivity> mActivityRule =
            new ActivityTestRule<>(TestbedActivity.class);

    @Before
    public void init() {

    }

    @Test
    public void computeDistanceTest() {
        double distance = Rooms.distanceBetweenTwoLatLng(Rooms.ROOMS_LOCATIONS.get("INR_0_11").getLocation(), Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation());
        assertEquals(256.9647560362717, distance, 1e-4);
    }

    @Test
    public void checkIfUserIsInRoom() {

        assertTrue(Rooms.checkIfUserIsInRoom());
    }

    @Test
    public void checkIfUserIsInRoomReturnsFalseWhenNoCourseTest() {
        Player.get().setCourses(new ArrayList<Constants.Course>());
        studentSchedule = new ArrayList<>();
        assertFalse(Rooms.checkIfUserIsInRoom());
    }



    @Test
    public void checkIfUserIsInRoomReturnsFalseWhenNoWeekEventTest() {
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        studentSchedule = new ArrayList<WeekViewEvent>();
        assertFalse(Rooms.checkIfUserIsInRoom());
    }
}
