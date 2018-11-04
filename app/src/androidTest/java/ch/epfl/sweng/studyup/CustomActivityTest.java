package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class CustomActivityTest {

    @Rule
    public ActivityTestRule<CustomActivity> rule =
            new ActivityTestRule<>(CustomActivity.class, true, false);

    @Before
    public void enableMock() { Utils.isMockEnabled = true; }

    @After
    public void disableMock() {
        Utils.isMockEnabled = false;
    }


    @Test
    public void testChooseGalleryImage() {

        rule.launchActivity(new Intent());

        onView(withId(R.id.pic_btn)).perform(click());

        onView(withText("Gallery")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }
}