package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;


import com.alamkanak.weekview.WeekViewEvent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom; //Random int library

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.map.Room;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.TestbedActivity;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.Utils.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class FirestoreTest {
    private static final String TAG = FirestoreTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<TestbedActivity> rule =
            new ActivityTestRule<>(TestbedActivity.class);

    private static final String TEST_SCIPER = "123456";
    private static final int TEST_LEVEL = 42;
    private static final String TEST_SECTION = "IN";
    private static final int TEST_XP = 4137;
    private static final String TEST_YEAR = "BA1";

    private static Map<String, Object> TEST_DATA = null;

    @BeforeClass
    public static void enableMock() {
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void disableMock() { MOCK_ENABLED = false; }


    public static void resetTestValues() {
        TEST_DATA = new HashMap<>();
        TEST_DATA.put(FB_FIRSTNAME, INITIAL_FIRSTNAME);
        TEST_DATA.put(FB_LASTNAME, INITIAL_LASTNAME);
        TEST_DATA.put(FB_LEVEL, TEST_LEVEL);
        TEST_DATA.put(FB_SECTION, TEST_SECTION);
        TEST_DATA.put(FB_XP, TEST_XP);
        TEST_DATA.put(FB_YEAR, TEST_YEAR);
        TEST_DATA.put(FB_CURRENCY, INITIAL_CURRENCY);
        TEST_DATA.put(FB_ROLE, Role.student);
    }


    @Before
    public void setup() {
        Player.get().resetPlayer();
        Player.get().setRole(Role.student);
        resetTestValues();
    }
    @After
    public void cleanupFirebase() {

        Firestore.get().deleteUserFromDatabase(TEST_SCIPER);
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

        resetTestValues();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLowTest() throws Exception {

        Player.get().setSciperNum("99999");
        Firestore.get().syncPlayerData();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHighTest() throws Exception {
        Player.get().setSciperNum("1000000");
        Firestore.get().syncPlayerData();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperNegativeTest() throws Exception {
        Player.get().setSciperNum("-42");
        Firestore.get().syncPlayerData();
    }


    @Test
    public void deleteUserTest() throws Exception {
        String maxSciper = String.valueOf(MAX_SCIPER);
        Player.get().setSciperNum(maxSciper);
        Player.get().setFirstName("John");
        Player.get().setLastName("Doe");
        Firestore.get().syncPlayerData();
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

        Player.get().addExperience(XP_STEP, null);
        Firestore.get().deleteUserFromDatabase(maxSciper);
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);
        Firestore.get().getData(MAX_SCIPER);
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

        resetTestValues();
        for (Map.Entry<String, Object> entry : TEST_DATA.entrySet()) {
            assert (DB_STATIC_INFO.get(entry.getKey()) == entry.getValue());
        }
    }


    public void addNewUserToDBTest() throws Exception {
        String maxSciper = String.valueOf(MAX_SCIPER);
        Player.get().setSciperNum(maxSciper);
        Player.get().setFirstName("John");
        Player.get().setLastName("Doe");
        Firestore.get().syncPlayerData();
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);
        Player.get().addCurrency(ThreadLocalRandom.current().nextInt(1, 1000 + 1), null);
        Player.get().addCurrency(ThreadLocalRandom.current().nextInt(1, 1000 + 1), null);
        Firestore.get().deleteUserFromDatabase(maxSciper);
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);
        Firestore.get().syncPlayerData();
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);
        Firestore.get().getData(MAX_SCIPER);
        waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);


        //Check if values are the initial ones
        assertEquals(INITIAL_CURRENCY, Player.get().getCurrency());
        assertEquals(INITIAL_XP, Player.get().getExperience());

        //Check if the names are the correct ones
        assertEquals("John", DB_STATIC_INFO.get(FB_FIRSTNAME));
        assertEquals("Doe", DB_STATIC_INFO.get(FB_LASTNAME));
        assertEquals("JonhDoe89012345", DB_STATIC_INFO.get(FB_USERNAME));
    }

    @Test
    public void addXpPlayerAndCurrencyTest() {
        Player.get().resetPlayer();

        assertEquals(INITIAL_XP, Player.get().getExperience());
        assertEquals(INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(INITIAL_CURRENCY, Player.get().getCurrency());

        assertEquals(0.0, Player.get().getLevelProgress(), 10e-6);

        Player.get().addExperience(XP_TO_LEVEL_UP / 2, null);
        assertEquals(INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(XP_TO_LEVEL_UP / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addExperience(XP_TO_LEVEL_UP, null);
        assertEquals(INITIAL_LEVEL + 1, Player.get().getLevel());
        assertEquals(CURRENCY_PER_LEVEL, Player.get().getCurrency());
        assertEquals((XP_TO_LEVEL_UP * 3) / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addCurrency(100, null);
        assertEquals(CURRENCY_PER_LEVEL + 100, Player.get().getCurrency());
    }

    @Ignore
    public void testFunctionnality() {
        List<WeekViewEvent> periods = getSimpleSchedule();
        Course c = Course.FakeCourse;
        List<Course> fakeCourseList = new ArrayList<>();
        fakeCourseList.add(c);

        Firestore.get().deleteCourse(c);

        // Verifying that course has been deleted
        Player.get().setRole(Role.student);
        Player.get().setCourses(fakeCourseList);
        Firestore.get().getCoursesSchedule(null, Role.student);
        waitAndTag(1000, TAG);
        assert Player.get().getScheduleStudent().isEmpty() : "The schedule should be empty as the only enrolled course has just been removed.";

        // Verifying that we can add a course
        Firestore.get().setCourseTeacher(c);
        waitAndTag(500, "Waiting for the course to be added.");
        Firestore.get().addEventsToCourse(c, periods);
        waitAndTag(500, "Waiting for the course's periods to be added.");
        Firestore.get().getCoursesSchedule(null, Role.student);
        waitAndTag(1000, TAG);
        assert periods.equals(Player.get().getScheduleStudent());

        //Verifying that we can change course schedule
        periods.get(0).setLocation("CO_2");
        periods.get(1).setLocation("CO_2");
        Firestore.get().addEventsToCourse(c, periods);
        waitAndTag(500, "Waiting for the course's periods to be updated.");
        Firestore.get().getCoursesSchedule(null, Role.student);
        waitAndTag(1000, TAG);
        assert periods.equals(Player.get().getScheduleStudent());
    }

    private List<WeekViewEvent> getSimpleSchedule() {
        final String room = "CO_1";
        List<WeekViewEvent> periods = new ArrayList<>();
        Date d1 = new Date();
        d1.setTime(123);
        Date d2 = new Date();
        d2.setTime(1234);
        Calendar end1 = new GregorianCalendar(2018,11,26);
        Calendar end2 = new GregorianCalendar(2018,11,27);
        WeekViewEvent w1 = new WeekViewEvent(0, Course.FakeCourse.name(), room, Calendar.getInstance(), end1);
        WeekViewEvent w2 = new WeekViewEvent(0, Course.FakeCourse.name(), room, Calendar.getInstance(), end2);
        periods.add(w1);
        periods.add(w2);

        return periods;
    }
}