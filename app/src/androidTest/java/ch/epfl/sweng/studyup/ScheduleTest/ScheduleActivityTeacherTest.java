package ch.epfl.sweng.studyup.ScheduleTest;

import android.content.Intent;
import android.graphics.Point;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Display;
import android.view.View;

import com.alamkanak.weekview.WeekViewEvent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ScheduleActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.teacher.ScheduleActivityTeacher.COURSE_NAME_INTENT_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ScheduleActivityTeacherTest {
    @Rule
    public final ActivityTestRule<ScheduleActivityTeacher> mActivityRule =
            new ActivityTestRule<>(ScheduleActivityTeacher.class, true, false);

    @BeforeClass
    public static void enableMock(){
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void disableMock(){
        MOCK_ENABLED = false;
    }

    @Before
    public void resetPlayerBefore(){
        Player.get().resetPlayer();
    }

    @After
    public void resetPlayerAfter(){
        Player.get().resetPlayer();
    }

    @Test
    public void addEventAndRemoveEventTest(){
        mActivityRule.launchActivity(new Intent().putExtra(COURSE_NAME_INTENT_SCHEDULE, Constants.Course.SWENG.name()));


        assertEquals(0, mActivityRule.getActivity().getWeekViewEvents().size());

        Display display = mActivityRule.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        onView(withId(R.id.weekView)).perform(clickXY(width - 8, height/2));
        Utils.waitAndTag(1000, "ScheduleActivityStudentTest");
        assertEquals(1, mActivityRule.getActivity().getWeekViewEvents().size());

        onView(withId(R.id.weekView)).perform(clickXY(width - 8, height/2));
        Utils.waitAndTag(1000, "ScheduleActivityStudentTest");
        assertEquals(0, mActivityRule.getActivity().getWeekViewEvents().size());
    }

    @Test
    public void saveButtonTest(){
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.fab)).perform(click());
    }

    @Test
    public void updateScheduleTest(){
        mActivityRule.launchActivity(new Intent());
        assertEquals(0, mActivityRule.getActivity().getWeekViewEvents().size());

        final List<WeekViewEvent> events = new ArrayList<>();

        int day = 19;
        int hour = 10;
        int month = 10;
        int year = 2018;

        Calendar eventStart = Calendar.getInstance();
        eventStart.set(Calendar.YEAR, year);
        eventStart.set(Calendar.MONTH, month);
        eventStart.set(Calendar.DAY_OF_MONTH, day);
        eventStart.set(Calendar.HOUR_OF_DAY, hour);
        eventStart.set(Calendar.MINUTE, 0);

        Calendar eventEnd = Calendar.getInstance();
        eventEnd.set(Calendar.YEAR, year);
        eventEnd.set(Calendar.MONTH, month);
        eventEnd.set(Calendar.DAY_OF_MONTH, day);
        eventEnd.set(Calendar.HOUR_OF_DAY, hour);
        eventEnd.set(Calendar.MINUTE, 59);

        events.add(new WeekViewEvent(0, "Sweng", "CO_0_1", eventStart, eventEnd));

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().updateSchedule(events);
            }
        });

        Utils.waitAndTag(1000, "ScheduleActivityStudentTest");
        assertEquals(1, mActivityRule.getActivity().getWeekViewEvents().size());
    }

    private ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}
