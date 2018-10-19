package ch.epfl.sweng.studyup;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;

@RunWith(AndroidJUnit4.class)
public class DisplayQuestionActivityTest {

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class);

    @Before
    public void initiateIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

}
