package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.kosalgeek.android.caching.FileCacher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.settings.ChooseColorActivity;
import ch.epfl.sweng.studyup.settings.CourseSelectionActivity;
import ch.epfl.sweng.studyup.settings.SettingsActivity;

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
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("HardCodedStringLiteral")
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
        onView(ViewMatchers.withId(R.id.logout_button)).perform(click());
        assertTrue(!persistLogin.hasCache());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void closeButtonTest() {
        onView(withId(R.id.close_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void testLanguageChoosingPopupFrench() {
        onView(withId(R.id.languageChoiceButton)).perform(click());
        onView(withText(LANGUAGES[1]))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(HomeActivity.class.getName()));
    }

    // Test that must run at last to make the tests run in the basic language (english)
    @Test
    public void z_testLanguageChoosingPopupEnglish() {
        onView(withId(R.id.languageChoiceButton)).perform(click());
        onView(withText(LANGUAGES[0]))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(HomeActivity.class.getName()));
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

    @Test
    public void testSetLocale() {
        setLocale("en", mActivityRule.getActivity());

        mActivityRule.getActivity().finish();
        mActivityRule.getActivity().startActivity(mActivityRule.getActivity().getIntent());

        assertEquals("en", mActivityRule.getActivity().getResources().getConfiguration().locale.toString());
    }
}
