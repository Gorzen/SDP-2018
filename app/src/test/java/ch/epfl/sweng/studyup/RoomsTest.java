package ch.epfl.sweng.studyup;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
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
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RoomsTest {
    private final String CO1 = "CO_1_1";
    private final String CO3 = "CO_1_3";


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
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        Calendar beforeCurrTime = Calendar.getInstance();
        beforeCurrTime.set(Calendar.HOUR_OF_DAY, beforeCurrTime.get(Calendar.HOUR_OF_DAY) - 1);
        Calendar afterCurrTime = Calendar.getInstance();
        afterCurrTime.set(Calendar.HOUR_OF_DAY, afterCurrTime.get(Calendar.HOUR_OF_DAY) + 1);
        Calendar unrelatedTime1 = Calendar.getInstance();
        unrelatedTime1.set(Calendar.HOUR_OF_DAY, unrelatedTime1.get(Calendar.HOUR_OF_DAY) + 4);
        Calendar unrelatedTime2 = Calendar.getInstance();
        unrelatedTime2.set(Calendar.HOUR_OF_DAY, unrelatedTime2.get(Calendar.HOUR_OF_DAY) + 5);
        WeekViewEvent weekViewEvent1 = new WeekViewEvent(0, Constants.Course.Algebra.name() + "\n" + CO1, beforeCurrTime, afterCurrTime);
        WeekViewEvent weekViewEvent2 = new WeekViewEvent(1, Constants.Course.Algebra.name() + "\n" + CO3, unrelatedTime1, unrelatedTime2);
        assertTrue(Rooms.checkIfUserIsInRoom(new ArrayList<>(Arrays.asList(weekViewEvent1, weekViewEvent2))));
    }

    @Test
    public void checkifUserIsInRoomReturnsFalseWhenNoCourseTest() {
        Player.get().setCourses(new ArrayList<Constants.Course>());
        assertFalse(Rooms.checkIfUserIsInRoom(new ArrayList<WeekViewEvent>()));
    }

    @Test
    public void checkifUserIsInRoomReturnsFalseWhenNoWeekEventTest() {
        Calendar beforeCurrTime = Calendar.getInstance();
        beforeCurrTime.set(Calendar.HOUR_OF_DAY, beforeCurrTime.get(Calendar.HOUR_OF_DAY) - 1);
        Calendar afterCurrTime = Calendar.getInstance();
        afterCurrTime.set(Calendar.HOUR_OF_DAY, afterCurrTime.get(Calendar.HOUR_OF_DAY) + 1);
        WeekViewEvent weekViewEvent1 = new WeekViewEvent(0, Constants.Course.Algebra.name() + "\n" + CO1, beforeCurrTime, afterCurrTime);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        assertFalse(Rooms.checkIfUserIsInRoom(new ArrayList<>(Arrays.asList(weekViewEvent1))));
    }
}
