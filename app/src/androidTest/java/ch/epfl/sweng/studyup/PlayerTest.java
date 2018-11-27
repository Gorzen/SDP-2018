package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants.*;
import ch.epfl.sweng.studyup.utils.TestbedActivity;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PlayerTest {

    @Rule
    public final ActivityTestRule<TestbedActivity> rule =
            new ActivityTestRule<>(TestbedActivity.class);

    @Before
    public void setup() {
        Player.get().resetPlayer();
    }

    @Test
    public void addItemTest() {
        Player.get().addItem(Items.XP_POTION);
        assertEquals(Player.get().getItems().get(0), Items.XP_POTION);
    }

    @Test
    public void consumeItemXpTest() throws Exception {
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
        assert(Player.get().getCoursesEnrolled().equals(testCourseList));
    }

    @Test
    public void addTeachingCourseTest() {
        Player.get().setRole(Role.teacher);
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Course.Blacksmithing);
        Player.get().setCourses(testCourseList);
        assert(Player.get().getCoursesEnrolled().equals(testCourseList));
    }
}
