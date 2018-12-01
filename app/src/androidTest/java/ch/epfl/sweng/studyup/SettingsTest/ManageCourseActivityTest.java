package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ManageCourseActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Algebra;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Blacksmithing;
import static ch.epfl.sweng.studyup.utils.Constants.Course.Ecology;
import static ch.epfl.sweng.studyup.utils.Constants.Course.SWENG;
import static ch.epfl.sweng.studyup.utils.Constants.Role.teacher;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ManageCourseActivityTest {
    @Rule
    public final ActivityTestRule<ManageCourseActivity> mActivityRule =
            new ActivityTestRule<>(ManageCourseActivity.class);

    @BeforeClass
    public static void setupTests() {
        Player.get().resetPlayer();
        Player.get().setRole(teacher);
        Firestore.get().syncPlayerData();
        Player.get().setCourses(Lists.newArrayList(Ecology));

        Firestore.get().updateRemotePlayerDataFromLocal();
        Utils.waitAndTag(2000, ManageCourseActivityTest.class.getName());
    }

    @Before
    public void waitForFirebase(){
        Utils.waitAndTag(10000, ManageCourseActivityTest.class.getName());
    }

    @Ignore
    public void testPlayerCoursesAreChecked() {
        onView(withText(SWENG.name())).check(matches(not((isChecked()))));
        onView(withText(Ecology.name())).check(matches(not((isChecked()))));
        onView(withText(Algebra.name())).check(matches((isChecked())));
        onView(withText(Blacksmithing.name())).check(matches((isChecked())));
    }

    @Ignore
    public void testCourseSelection() {
        onView(withText(Ecology.name())).perform(click());

        onView(withText(R.string.save_value)).perform(click());
        assert(Player.get().getCoursesTeached().contains(Ecology));
    }

    @Test
    public void testManageCourse(){
        
    }
}
