package ch.epfl.sweng.studyup;

import android.provider.MediaStore;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RadioGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void clearChecks() {
        RadioGroup roles = mActivityRule.getActivity().findViewById(R.id.StudentOrTeacherButtons);
        roles.clearCheck();
    }

    @Test
    public void userHasToChooseRoleBeforeContinuing() {
        onView(withId(R.id.loginButton)).perform(click());
        //If no exception is thrown that would mean an intent has bee started
        // (and thus the current activity being changed)
        try {
            intended(hasComponent(LoginActivity.class.getName()));
        } catch(NullPointerException e) {
            return;
        }
        fail();
    }

    @Test
    public void teacherRoleIsStored() {
        onView(withId(R.id.teacher)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());

        assertTrue(Player.get().getRole());
    }

    @Test
    public void studentRoleIsStored() {
        onView(withId(R.id.student)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());

        assertFalse(Player.get().getRole());
    }
}