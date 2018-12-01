package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ManageCourseActivity;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE_REQUESTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_REQUESTED_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.Role.teacher;
import static ch.epfl.sweng.studyup.utils.Constants.SUPER_USERS;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ManageCourseActivityTest {
    @Rule
    public final ActivityTestRule<ManageCourseActivity> mActivityRule =
            new ActivityTestRule<>(ManageCourseActivity.class);

    @BeforeClass
    public static void enableMock(){
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void disableMock(){
        MOCK_ENABLED = false;
    }

    @Before
    public void resetPlayer(){
        resetPlayerInfo();
    }

    @After
    public void resetPlayerAndRemoveSciperFromSuperUsers(){
        SUPER_USERS.remove(Player.get().getSciperNum());
        resetPlayerInfo();
    }

    @Test
    public void testManageCourseNormalUser() {
        testManageCourseSample(R.id.listViewOtherCourses, R.id.send_course_request, -1, 1, 0);
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

        //Add pending course
        testManageCourseSample(R.id.listViewOtherCourses, R.id.send_course_request, -1, 1, 0);

        //Accept pending course
        testManageCourseSample(R.id.listViewPendingCourses, R.id.acceptCourse, 0, -1, 1);
    }

    private void testManageCourseSample(int listViewToClickOn, int buttonToClick, int otherCoursesChangeCount, int pendingCoursesChangeCount, int acceptedCoursesChangeCount) {
        int numberOfOtherCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewOtherCourses)).getCount();
        int numberOfPendingCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewPendingCourses)).getCount();
        int numberOfAcceptedCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewAcceptedCourses)).getCount();

        onData(anything()).inAdapterView(withId(listViewToClickOn))
                .atPosition(0)
                .onChildView(withId(buttonToClick))
                .perform(scrollTo())
                .perform(click());

        Utils.waitAndTag(1000, ManageCourseActivityTest.class.getName());

        int newNumberOfOtherCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewOtherCourses)).getCount();
        int newNumberOfPendingCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewPendingCourses)).getCount();
        int newNumberOfAcceptedCourses = ((NonScrollableListView)mActivityRule.getActivity().findViewById(R.id.listViewAcceptedCourses)).getCount();

        assertEquals(numberOfOtherCourses + otherCoursesChangeCount, newNumberOfOtherCourses);
        assertEquals(numberOfPendingCourses + pendingCoursesChangeCount, newNumberOfPendingCourses);
        assertEquals(numberOfAcceptedCourses + acceptedCoursesChangeCount, newNumberOfAcceptedCourses);
    }

    private void resetCourseRequest(){
        final DocumentReference userRequestsRef = Firestore.get().getDb().collection(FB_COURSE_REQUESTS).document(Player.get().getSciperNum());
        userRequestsRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> userData = documentSnapshot.getData();
                        userData.put(FB_REQUESTED_COURSES, new ArrayList<>());
                        userRequestsRef.set(userData);
                    }
                });
    }

    private void resetPlayerInfo(){
        //Reset player locally and on Firebase
        Player.get().resetPlayer();
        Player.get().setRole(teacher);

        resetCourseRequest();
        Firestore.get().updateRemotePlayerDataFromLocal();
        Utils.waitAndTag(2000, ManageCourseActivityTest.class.getName());
    }
}
