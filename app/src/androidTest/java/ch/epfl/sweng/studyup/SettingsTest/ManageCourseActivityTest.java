package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ManageCourseActivity;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.Role.teacher;
import static ch.epfl.sweng.studyup.utils.Constants.SUPER_USERS;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ManageCourseActivityTest {
    @Rule
    public final ActivityTestRule<ManageCourseActivity> mActivityRule =
            new ActivityTestRule<>(ManageCourseActivity.class);

    @BeforeClass
    public static void setupTests() {
        //Reset player locally and on Firebase
        Player.get().resetPlayer();
        Player.get().setRole(teacher);

        Firestore.get().updateRemotePlayerDataFromLocal();
        Utils.waitAndTag(2000, ManageCourseActivityTest.class.getName());
    }

    @Before
    public void waitFirebase(){
        Utils.waitAndTag(1000, ManageCourseActivityTest.class.getName());
    }

    @After
    public void removeTestSciperFromSuperUsers(){
        //Always remove test SCIPER from Superusers, even if a test fails
        SUPER_USERS.remove(Player.get().getSciperNum());
    }

    @Test
    public void testManageCourseNormalUser() throws Exception {
        int numberOfOtherCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewOtherCourses)).getCount();
        int numberOfPendingCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewPendingCourses)).getCount();

        onData(anything()).inAdapterView(withId(R.id.listViewOtherCourses))
                .atPosition(0)
                .onChildView(withId(R.id.send_course_request))
                .perform(click());

        Utils.waitAndTag(1000, ManageCourseActivityTest.class.getName());

        int newNumberOfOtherCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewOtherCourses)).getCount();
        int newNumberOfPendingCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewPendingCourses)).getCount();

        assertEquals(numberOfOtherCourses - 1, newNumberOfOtherCourses);
        assertEquals(numberOfPendingCourses + 1, newNumberOfPendingCourses);
    }

    @Test
    public void testManageCourseSuperUser(){
        SUPER_USERS.add(Player.get().getSciperNum());
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity().setupListViews();
            }
        });
        Utils.waitAndTag(1000, ManageCourseActivityTest.class.getName());
    }
}
