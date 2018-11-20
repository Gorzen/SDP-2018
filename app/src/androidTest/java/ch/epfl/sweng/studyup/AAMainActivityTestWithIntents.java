package ch.epfl.sweng.studyup;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.CURR_DISPLAY;
import static ch.epfl.sweng.studyup.utils.Constants.LEVEL_DISPLAY;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.Constants.XP_TO_LEVEL_UP;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AAMainActivityTestWithIntents {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void  initIntent() {
        Intents.init();
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }


    @Test
    public void testToCustomActWithB1() {
        onView(withId(R.id.pic_btn)).perform(click());
        intended(hasComponent(CustomActivity.class.getName()));
    }

    @Test
    public void testToCustomActWithB2() {
        onView(withId(R.id.pic_btn2)).perform(click());
        intended(hasComponent(CustomActivity.class.getName()));
    }

}