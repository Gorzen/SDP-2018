package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.alamkanak.weekview.WeekViewEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.Constants.Role;
import ch.epfl.sweng.studyup.utils.TestbedActivity;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PlayerTest {

    @Rule
    public final ActivityTestRule<TestbedActivity> rule =
            new ActivityTestRule<>(TestbedActivity.class);

    @Before
    public void setup() {
        Player.get().resetPlayer();
        Player.get().setRole(Role.student);
        Firestore.get().getCoursesSchedule(rule.getActivity(), Player.get().getRole());
    }

    @Test
    public void addItemTest() {
        Player.get().addItem(Items.XP_POTION);
        assertEquals(Player.get().getItems().get(0), Items.XP_POTION);
    }

    @Test
    public void consumeItemXpTest() {
        Player.get().addItem(Items.XP_POTION);
        int xp = Player.get().getExperience();
        Player.get().consumeItem(Items.XP_POTION);
        assertEquals(xp + XP_STEP, Player.get().getExperience());
    }

    @Test
    public void consumeItemCurrencyTest() throws Exception {
        Player.get().addItem(Items.COIN_SACK);
        int currency = Player.get().getCurrency();
        Player.get().consumeItem(Items.COIN_SACK);
        assertEquals(currency + CURRENCY_PER_LEVEL, Player.get().getCurrency());
    }

    @Test
    public void enrollInCoursesTest() {
        Player.get().setRole(Role.student);
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Course.Blacksmithing);
        Player.get().setCourses(testCourseList);
        assertTrue(Player.get().getCoursesEnrolled().equals(testCourseList));
    }

    @Test
    public void addTeachingCourseTest() {
        Player.get().setRole(Role.teacher);
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Course.Blacksmithing);
        Player.get().setCourses(testCourseList);
        assertTrue(Player.get().getCoursesEnrolled().equals(testCourseList));
    }

    @Test
    public void addClickedInstantTest() {
        long instantTest = 12345;
        String keyTest = "SimpleKeyRTest";
        Player.get().addClickedInstant(keyTest, instantTest);
        assertTrue(Player.get().getClickedInstants().containsKey(keyTest));
        assertEquals(Long.valueOf(instantTest), Player.get().getClickedInstants().get(keyTest));
    }

    @Test
    public void getCurrentCourseLocationNoCourse() {
        Player.get().resetPlayer();
        assertNull(Player.get().getCurrentCourseLocation());
    }

    @Test
    public void getCurrentCourseLocationNoSchedule() {
        Player.get().resetPlayer();
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Course.Blacksmithing);
        Player.get().setCourses(testCourseList);
        assertNull(Player.get().getCurrentCourseLocation());
    }

    @Test
    public void getCurrentCourseLocationCorrectCourse() {
        Player.get().setRole(Constants.Role.student);
        Player.get().setCourses(Arrays.asList(Constants.Course.Algebra));

        Calendar beforeCurrTime = Calendar.getInstance();
        beforeCurrTime.set(Calendar.HOUR_OF_DAY, beforeCurrTime.get(Calendar.HOUR_OF_DAY) - 1);
        setCalendarToRightWeek(beforeCurrTime);
        Calendar afterCurrTime = Calendar.getInstance();
        afterCurrTime.set(Calendar.HOUR_OF_DAY, afterCurrTime.get(Calendar.HOUR_OF_DAY) + 1);
        setCalendarToRightWeek(afterCurrTime);

        String CO1 = "CO_1_1";

        WeekViewEvent weekViewEvent = new WeekViewEvent(0, Constants.Course.Algebra.name(), CO1, beforeCurrTime, afterCurrTime);
        Player.get().setScheduleStudent(new ArrayList<>(Arrays.asList(weekViewEvent)));

        assertEquals(CO1, Player.get().getCurrentCourseLocation());
    }

    private void setCalendarToRightWeek(Calendar calendar) {
        calendar.set(Calendar.YEAR, Constants.YEAR_OF_SCHEDULE);
        calendar.set(Calendar.MONTH, Constants.MONTH_OF_SCHEDULE);
        calendar.set(Calendar.WEEK_OF_MONTH, Constants.WEEK_OF_MONTH_SCHEDULE);
    }
}
