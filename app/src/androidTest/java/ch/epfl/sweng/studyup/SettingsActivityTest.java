package ch.epfl.sweng.studyup;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.kosalgeek.android.caching.FileCacher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static org.junit.Assert.assertTrue;

public class SettingsActivityTest {

    private SettingsActivity activity;

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void initiateIntents() {
        Intents.init();
        activity = mActivityRule.getActivity();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void logoutButton() {
        FileCacher<String[]> persistLogin = new FileCacher<>(mActivityRule.getActivity(), PERSIST_LOGIN_FILENAME);
        try {
            String[] s = {"Test"};
            persistLogin.writeCache(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.logoutbutton)).perform(click());
        assertTrue(!persistLogin.hasCache());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void closeButtonTest() {
        onView(withId(R.id.back_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
