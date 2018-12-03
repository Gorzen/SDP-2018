package ch.epfl.sweng.studyup.CustomActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.CustomActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class CustomActivityTest2 {

    @Rule
    public ActivityTestRule<CustomActivity> rule =
            new ActivityTestRule<>(CustomActivity.class, true, false);

    @Before
    public void enableMock() {
        MOCK_ENABLED = true;
    }

    @After
    public void disableMock() {
        MOCK_ENABLED = false;
    }


    @Test
    public void testChooseGalleryImage() {

        rule.launchActivity(new Intent());

        onView(ViewMatchers.withId(R.id.pic_btn)).perform(click());

        onView(withText(InstrumentationRegistry.getTargetContext().getString(R.string.gallery))).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(scrollTo(), click());
    }
}