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
    private int currYear;
    private int currMonth;
    private int currDay;
    private int currHour; //24hours format
    private int currMinute;
    private Calendar currTime;


    @Before
    public void init() {
        currTime = Calendar.getInstance();
        currYear = currTime.get(Calendar.YEAR);
        currMonth = currTime.get(Calendar.MONTH);
        currDay = currTime.get(Calendar.DAY_OF_WEEK);
        currHour = currTime.get(Calendar.HOUR_OF_DAY);
        currMinute = currTime.get(Calendar.MINUTE);
    }

    @Test
    public void computeDistanceTest() {
        double distance = Rooms.distanceBetweenTwoLatLng(Rooms.ROOMS_LOCATIONS.get("INR_0_11").getLocation(), Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation());
        assertEquals(256.9647560362717, distance, 1e-4);
    }

    @Test
    public void checkifUserIsInRoomReturnsFalseWhenNoCourseTest() {
        Date date = new Date();
        Calendar currTime = Calendar.getInstance();
        WeekViewEvent weekViewEvent = new WeekViewEvent(0, "RoomTest", 2018, Calendar.)
        Rooms.checkIfUserIsInRoom()
    }

    private void shiftCalendarHour()
}
