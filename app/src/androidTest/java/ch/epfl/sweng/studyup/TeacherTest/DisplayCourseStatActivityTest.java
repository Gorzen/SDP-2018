package ch.epfl.sweng.studyup.TeacherTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.teacher.DisplayCourseStatsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DisplayCourseStatActivityTest {

    @Rule
    public final ActivityTestRule<DisplayCourseStatsActivity> mActivityRule =
            new ActivityTestRule<>(DisplayCourseStatsActivity.class, true, false);

    @Test
    public void backButtonTest() {
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.back_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
