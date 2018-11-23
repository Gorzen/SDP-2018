package ch.epfl.sweng.studyup;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

import ch.epfl.sweng.studyup.utils.Rooms;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RoomsTest {
    private Calendar;


    @Before
    public void init() {

    }

    @Test
    public void computeDistanceTest() {
        double distance = Rooms.distanceBetweenTwoLatLng(Rooms.ROOMS_LOCATIONS.get("INR_0_11").getLocation(), Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation());
        assertEquals(256.9647560362717, distance, 1e-4);
    }

    @Test
    public void checkifUserIsInRoomReturnsFalseWhenNoCourseTest() {
        Calendar calendar_13h = buildCalendar(2018, 10, 1, 13, 0);
        Calendar calendar_14h = buildCalendar(2018, 10, 1, 14, 0);
        Calendar calendar_16h = buildCalendar(2018, 10, 1, 16, 0);
        Calendar calendar_17h = buildCalendar(2018, 10, 1, 17, 0);
        Calendar calendar_14h30 = buildCalendar(2018, 10, 1, 14, 30);
        WeekViewEvent weekViewEvent1 = new WeekViewEvent(0, "RoomTest", calendar_13h, calendar_14h);
        WeekViewEvent weekViewEvent2 = new WeekViewEvent(0, "RoomTest", calendar_16h, calendar_17h);
        Rooms.checkIfUserIsInRoom()
    }

    private Calendar buildCalendar(int year, int month, int day, int hour, int minute) {
        Calendar mockCalendar = Calendar.getInstance();
        mockCalendar.set(Calendar.YEAR, year);
        mockCalendar.set(Calendar.MONTH, month);
        mockCalendar.set(Calendar.DAY_OF_MONTH, day);
        mockCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mockCalendar.set(Calendar.MINUTE, minute);
        return mockCalendar;
    }
}
