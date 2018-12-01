package ch.epfl.sweng.studyup.SettingsTest;

import android.content.Intent;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
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
import static ch.epfl.sweng.studyup.utils.Constants.SUPER_USERS;
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
        Player.get().setCourses(Lists.newArrayList(Ecology));

        Firestore.get().updateRemotePlayerDataFromLocal();
        Utils.waitAndTag(2000, ManageCourseActivityTest.class.getName());
    }

    @Before
    public void waitForFirebase(){
        mActivityRule.getActivity().addRequest(Algebra.name(), Player.get().getSciperNum(), Player.get().getFirstName(), Player.get().getLastName());
        Utils.waitAndTag(1000, ManageCourseActivityTest.class.getName());

        mActivityRule.getActivity().getAllRequests();
        Utils.waitAndTag(2000, ManageCourseActivityTest.class.getName());
    }

    @After
    public void removeTestSciperFromSuperUsers(){
        SUPER_USERS.remove(Player.get().getSciperNum());
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
    public void testManageCourseNormalUser(){
        Utils.waitAndTag(10000, ManageCourseActivityTest.class.getName());
    }

    @Ignore
    public void testManageCourseSuperUser(){
        SUPER_USERS.add(Player.get().getSciperNum());
        mActivityRule.getActivity().setupListViews();
        Utils.waitAndTag(10000, ManageCourseActivityTest.class.getName());
    }
}
