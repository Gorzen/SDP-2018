package ch.epfl.sweng.studyup.LocationTest;

import android.app.job.JobParameters;
import android.location.Location;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.TestbedActivity;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Rooms;

import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class ServiceGetLocationAndCheckRoomTest {
    private BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);
    private OnSuccessListener<Location> onSuccessListener = getLocation.onSuccessListener;
    private LatLng alaska = new LatLng(64.2008, 149.4937);
    private final String CO1 = "CO_1_1";
    private final String CO3 = "CO_1_3";
    private Calendar beforeCurrTime;
    private Calendar afterCurrTime;
    private Calendar unrelatedTime1;
    private Calendar unrelatedTime2;
    private Location location;

    @Rule
    public final ActivityTestRule<TestbedActivity> mActivityRule2 =
            new ActivityTestRule<>(TestbedActivity.class);

    @Before
    public void init() {
        beforeCurrTime = Calendar.getInstance();
        beforeCurrTime.set(Calendar.HOUR_OF_DAY, beforeCurrTime.get(Calendar.HOUR_OF_DAY) - 1);
        afterCurrTime = Calendar.getInstance();
        afterCurrTime.set(Calendar.HOUR_OF_DAY, afterCurrTime.get(Calendar.HOUR_OF_DAY) + 1);
        unrelatedTime1 = Calendar.getInstance();
        unrelatedTime1.set(Calendar.HOUR_OF_DAY, unrelatedTime1.get(Calendar.HOUR_OF_DAY) + 4);
        unrelatedTime2 = Calendar.getInstance();
        unrelatedTime2.set(Calendar.HOUR_OF_DAY, unrelatedTime2.get(Calendar.HOUR_OF_DAY) + 5);
    }

    @Test
    public void getLocationNoCrashWithBadParams() {
        BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);

        JobParameters jobParameters = getLocation.doInBackground(new Void[]{});
        assertNull(jobParameters);

        getLocation.onPostExecute(null);
    }

    @Test
    public void backgroundLocationNoCrashWithBadParams(){
        BackgroundLocation backgroundLocation = new BackgroundLocation();
        backgroundLocation.onStartJob(null);
        backgroundLocation.onStopJob(null);
    }

    @Test
    public void onSuccessListenerTestNullLocation(){
        onSuccessListener.onSuccess(null);
    }

    @Test
    public void onSuccessListenerTestRoomOfPlayer(){
        final int exp = Player.get().getExperience();
        final LatLng roomOfPlayer = Rooms.ROOMS_LOCATIONS.get(CO1).getLocation();
        setLocation(roomOfPlayer);

        Player.get().setRole(Constants.Role.student);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        WeekViewEvent weekViewEvent1 = new WeekViewEvent(0, Constants.Course.Algebra.name() + "\n" + CO1, beforeCurrTime, afterCurrTime);
        WeekViewEvent weekViewEvent2 = new WeekViewEvent(1, Constants.Course.Algebra.name() + "\n" + CO3, unrelatedTime1, unrelatedTime2);
        Player.get().setScheduleStudent(new ArrayList<>(Arrays.asList(weekViewEvent1, weekViewEvent2)));

        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                assertEquals(roomOfPlayer.latitude, POSITION.latitude, 0);
                assertEquals(roomOfPlayer.longitude, POSITION.longitude, 0);
                assertEquals(exp + 2 * XP_STEP, Player.get().getExperience());
            }
        });
    }

    @Test
    public void onSuccessListenerTestNotRoomOfPlayer(){
        final int exp = Player.get().getExperience();
        setLocation(alaska);

        Player.get().setRole(Constants.Role.student);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        Player.get().setScheduleStudent(new ArrayList<>(Arrays.asList(new WeekViewEvent(0, Constants.Course.Algebra.name() + "\n" + CO3, beforeCurrTime, afterCurrTime))));
        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                assertEquals(alaska.latitude, POSITION.latitude, 0);
                assertEquals(alaska.longitude, POSITION.longitude, 0);
                assertEquals(exp, Player.get().getExperience());
            }
        });
    }

    @Test
    public void checkIfUserIsInRoomReturnsFalseWhenNoEventMatchesTest() {
        final LatLng roomOfPlayer = Rooms.ROOMS_LOCATIONS.get(CO1).getLocation();
        setLocation(roomOfPlayer);
        WeekViewEvent weekViewEvent1 = new WeekViewEvent(0, Constants.Course.Algebra.name() + "\n" + CO1, unrelatedTime1, unrelatedTime2);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        Player.get().setScheduleStudent(new ArrayList<>(Arrays.asList(weekViewEvent1)));
        POSITION = new LatLng(location.getLatitude(), location.getLongitude());
        assertFalse(Rooms.checkIfUserIsInRoom());
    }

    @Test
    public void checkIfUserIsInRoomReturnsFalseWhenNoCourseTest() {
        final LatLng roomOfPlayer = Rooms.ROOMS_LOCATIONS.get(CO1).getLocation();
        setLocation(roomOfPlayer);
        Player.get().setRole(Constants.Role.student);
        Player.get().setCourses(new ArrayList<Constants.Course>());
        Player.get().setScheduleStudent(new ArrayList<WeekViewEvent>());
        POSITION = new LatLng(location.getLatitude(), location.getLongitude());
        assertFalse(Rooms.checkIfUserIsInRoom());
    }

    @Test
    public void checkIfUserIsInRoomReturnsFalseWhenNoWeekEventTest() {
        final LatLng roomOfPlayer = Rooms.ROOMS_LOCATIONS.get(CO1).getLocation();
        setLocation(roomOfPlayer);
        Player.get().setRole(Constants.Role.student);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));
        Player.get().setScheduleStudent(new ArrayList<WeekViewEvent>());
        POSITION = new LatLng(location.getLatitude(), location.getLongitude());
        assertFalse(Rooms.checkIfUserIsInRoom());
    }

    private void setLocation(LatLng roomOfPlayer) {
        location = new Location("Mock");
        location.setLatitude(roomOfPlayer.latitude);
        location.setLongitude(roomOfPlayer.longitude);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());
    }

}
