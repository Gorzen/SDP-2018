package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void userHasToChooseRoleBeforeContinuing() {
        onView(withId(R.id.loginButton)).perform(click());
        //If we're still on the loginActivity, that means we can still use the buttons
        onView(withId(R.id.student)).perform(click());
        onView(withId(R.id.teacher)).perform(click());
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