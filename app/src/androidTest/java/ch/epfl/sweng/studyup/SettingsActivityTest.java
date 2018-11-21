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
import static ch.epfl.sweng.studyup.utils.Constants.LANGUAGES;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static org.junit.Assert.assertTrue;

public class SettingsActivityTest {

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Before
    public void initiateIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    //@Test
    public void logoutButton() {
        FileCacher<String[]> persistLogin = new FileCacher<>(mActivityRule.getActivity(), PERSIST_LOGIN_FILENAME);
        try {
            String[] s = {"Test"};
            persistLogin.writeCache(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.logout_button)).perform(click());
        assertTrue(!persistLogin.hasCache());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void closeButtonTest() {
        onView(withId(R.id.close_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testLanguageChoosingPopup() {
        for(String s : LANGUAGES) {
            onView(withId(R.id.languageChoiceButton)).perform(click());
            onView(withText(s))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(click());
        }

        //If all worked, we could close the settings
        onView(withId(R.id.close_button)).perform(click());
    }

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
}
