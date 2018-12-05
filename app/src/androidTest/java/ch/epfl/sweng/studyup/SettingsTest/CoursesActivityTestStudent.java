package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.settings.CourseSelectionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Algebra;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Blacksmithing;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Ecology;
import static ch.epfl.sweng.studyup.utils.Constants.Course.SWENG;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;

public class CoursesActivityTestStudent {

    @Rule
    public final ActivityTestRule<CourseSelectionActivity> mActivityRule =
            new ActivityTestRule<>(CourseSelectionActivity.class);

    @BeforeClass
    public static void setupTests() {
        Player.get().setRole(Role.student);
        List<Course> testCourseList = new ArrayList<>();
        testCourseList.add(Algebra);
        testCourseList.add(Blacksmithing);
        Player.get().setCourses(testCourseList);
    }

    @Test
    public void testPlayerCoursesAreChecked() {
        onView(withText(SWENG.name())).check(matches(not((isChecked()))));
        onView(withText(Ecology.name())).check(matches(not((isChecked()))));
        onView(withText(Algebra.name())).check(matches((isChecked())));
        onView(withText(Blacksmithing.name())).check(matches((isChecked())));
    }

    @Test
    public void testCourseSelection() {
        onView(withText(SWENG.name())).perform(scrollTo()).perform(click());
        onView(withId(R.id.saveButton)).perform(scrollTo()).perform(click());

        assertTrue(Player.get().getCoursesEnrolled().contains(SWENG));
    }

    @Test
    public void backButtonTest(){
        onView(withId(R.id.back_button)).perform(click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
