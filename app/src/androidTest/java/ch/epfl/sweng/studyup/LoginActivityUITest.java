package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LoginActivityUITest {

    @Rule
    public final ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void a_userHasToChooseRoleBeforeContinuing() {
        onView(withId(R.id.loginButton)).perform(click());
        //If we're still on the loginActivity, that means we can still use the buttons
        onView(withId(R.id.student)).perform(click());
        onView(withId(R.id.teacher)).perform(click());
    }

    @Test
    public void studentRoleIsStored() {
        onView(withId(R.id.student)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        assertFalse(Player.get().getRole());
    }
}