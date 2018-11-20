package ch.epfl.sweng.studyup.CustomActivityTest;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

@RunWith(AndroidJUnit4.class)
public class CustomActivityTest2 {

    @Rule
    public ActivityTestRule<CustomActivity> rule =
            new ActivityTestRule<>(CustomActivity.class);

    @Test
    public void testChooseGalleryImage() {

        onView(ViewMatchers.withId(R.id.pic_btn)).perform(click());

        onView(withText("Gallery")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }
}