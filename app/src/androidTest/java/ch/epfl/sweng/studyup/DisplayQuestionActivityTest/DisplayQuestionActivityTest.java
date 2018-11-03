package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;

public class DisplayQuestionActivityTest {

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class,
                    true,
                    false);

    @Before
    public void setUp(){
        Log.d("DisplayQuestionActivityTest", "Started test");
        Intents.init();
    }

    @After
    public void cleanUp(){
        Log.d("DisplayQuestionActivityTest", "Finished test");
        Intents.release();
        mActivityRule.finishActivity();
        Utils.killApp("DisplayQuestionActivityTest");
        Utils.waitAndTag(1000, "DisplayQuestionActivityTest");
    }
}