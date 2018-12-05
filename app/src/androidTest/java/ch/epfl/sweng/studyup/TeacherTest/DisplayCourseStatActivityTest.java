package ch.epfl.sweng.studyup.TeacherTest;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.teacher.DisplayCourseStatsActivity;
import ch.epfl.sweng.studyup.utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DisplayCourseStatActivityTest {

    @Rule
    public final ActivityTestRule<DisplayCourseStatsActivity> mActivityRule =
            new ActivityTestRule<>(DisplayCourseStatsActivity.class, true, false);

    @Before
    public void putCourseInIntent() {
        Intent i = new Intent();
        i.putExtra(DisplayQuestionActivity.class.getName(), Constants.Course.SWENG.name());
        mActivityRule.launchActivity(i);
    }

    @AfterClass
    public static void clean() {
        Player.get().resetPlayer();
        Player.get().setRole(Constants.Role.student);
        Intents.release();
    }

    @Test
    public void backButtonTest() {
        onView(withId(R.id.back_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
