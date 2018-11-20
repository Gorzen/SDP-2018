package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static ch.epfl.sweng.studyup.utils.Constants.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class CoursesActivityTest {

    @Rule
    public final ActivityTestRule<CourseSelectionActivity> mActivityRule =
            new ActivityTestRule<>(CourseSelectionActivity.class);

    @BeforeClass
    public static void setupTests() {
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Course.Algebra);
        testCourseList.add(Course.Blacksmithing);
        Player.get().setCourses(testCourseList);
    }

    @Test
    public void testPlayerCoursesAreChecked() {
        onView(withText(Course.SWENG.name())).check(matches(not((isChecked()))));
        onView(withText(Course.Ecology.name())).check(matches(not((isChecked()))));
        onView(withText(Course.Algebra.name())).check(matches((isChecked())));
        onView(withText(Course.Blacksmithing.name())).check(matches((isChecked())));
    }

    @Test
    public void testCourseSelection() {
        onView(withText(Course.Ecology.name())).perform(click());

        onView(withText(R.string.save_value)).perform(click());
        assert(Player.get().getCourses().contains(Course.Ecology));
    }
}
