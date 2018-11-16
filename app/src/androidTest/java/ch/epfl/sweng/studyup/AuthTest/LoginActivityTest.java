package ch.epfl.sweng.studyup.AuthTest;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, false);

    @Before
    public void enableMock() {
        MOCK_ENABLED = true;
    }

    @After
    public void disableMock() {
        MOCK_ENABLED = false;
    }

    // Test handle failed login message}
    @Test
    public void testFailedLoginReport() {

        Intent badLoginIntent = new Intent();
        badLoginIntent.putExtra("login_message", "Failed Login");
        rule.launchActivity(badLoginIntent);
    }

    // Test handle no login message
    @Test
    public void testNoReport() {
        rule.launchActivity(new Intent());
    }

    // Test button click redirect
    @Test
    public void testLoginButtonRedirect() {
        rule.launchActivity(new Intent());
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click());
    }
}