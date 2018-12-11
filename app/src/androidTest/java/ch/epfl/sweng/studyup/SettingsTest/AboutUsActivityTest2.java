package ch.epfl.sweng.studyup.SettingsTest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.settings.AboutUsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AboutUsActivityTest2 {


    @Rule
    public final ActivityTestRule<AboutUsActivity> mActivityRule =
            new ActivityTestRule<>(AboutUsActivity.class);

    @Before
    public void initIntent() {
        Intents.init();
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }

    @Test
    public void clickBackButtonAboutUsTest() {
        onView(withId(R.id.back_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
