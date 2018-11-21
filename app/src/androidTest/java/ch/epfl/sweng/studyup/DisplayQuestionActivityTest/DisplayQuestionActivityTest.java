package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

public class DisplayQuestionActivityTest {

    protected Question sampleQuestion = new Question("id-test", "test", true, 0,Constants.Course.SWENG.name());
    protected final String TAG = "DisplayQuestionActivityTest";

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class,
                    true,
                    false);

    @Before
    public void clearQuestions(){
        Player.get().getAnsweredQuestion().clear();
        Utils.waitAndTag(500, "Waiting for the Toast to disappear in DisplayQuestionActivityTest");
    }

    @BeforeClass
    public static void setUp() {
        Log.d("DisplayQuestionActivityTest", "Started test");
        Intents.init();
        Utils.waitAndTag(1000, "DisplayQuestionActivityTest");
    }

    @AfterClass
    public static void cleanUp() {
        Log.d("DisplayQuestionActivityTest", "Finished test");
        Intents.release();
        Utils.waitAndTag(1000, "DisplayQuestionActivityTest");
    }

}