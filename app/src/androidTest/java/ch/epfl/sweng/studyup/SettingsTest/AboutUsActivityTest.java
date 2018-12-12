package ch.epfl.sweng.studyup.SettingsTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.settings.AboutUsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

public class AboutUsActivityTest {

    @Rule
    public final ActivityTestRule<AboutUsActivity> rule =
//            new ActivityTestRule<>(AboutUsActivity.class);
                new ActivityTestRule<>(AboutUsActivity .class, true, false);

    @Before
    public void enableMock() {
        MOCK_ENABLED = true;
    }

    @After
    public void disableMock() {
        MOCK_ENABLED = false;
    }

    @Test
    public void testSendMailPopup() {
        rule.launchActivity(new Intent());
        onView(withId(R.id.sendMailButton)).check(matches(isDisplayed())).perform(scrollTo(), click());
        //intended(not(hasAction(Intent.ACTION_CHOOSER)));
    }

}