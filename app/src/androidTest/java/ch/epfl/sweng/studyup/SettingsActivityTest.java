package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.design.widget.BottomNavigationView;
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
import java.util.Locale;


import ch.epfl.sweng.studyup.utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.content.ContextCompat.startActivity;
import static ch.epfl.sweng.studyup.utils.Constants.LANGUAGES;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("HardCodedStringLiteral")
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

    // Sorry
    /*@Test
    public void testLanguageChoosingPopup() {
        for(String s : LANGUAGES) {
            onView(withId(R.id.languageChoiceButton)).perform(click());
            onView(withText(s))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());
        }

        //If all worked, we could close the settings
        onView(withId(R.id.back_button)).perform(click());
    }*/

    @Test
    public void testCourseSelectionRedirect() {
        onView(withId(R.id.courseChoiceButton)).perform(click());
        intended(hasComponent(CourseSelectionActivity.class.getName()));
    }

    @Test
    public void testColorSelectionRedirect() {
        onView(withId(R.id.colorChoiceButton)).perform(click());
        intended(hasComponent(ChooseColorActivity.class.getName()));
    }

    @Test
    public void testSetLocale() {
        setLocale("en", activity);

        activity.finish();
        activity.startActivity(activity.getIntent());

        assertEquals("en", activity.getResources().getConfiguration().locale.toString());
    }
}
