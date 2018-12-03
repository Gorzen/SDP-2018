package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.ListView;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static org.hamcrest.Matchers.not;

public class CannotAnswerTwiceTest {
    private Question testQuestion = new Question("abc", "test", true, 0, Constants.Course.SWENG.name(), "en");

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class,
                    true,
                    false);

    @Before
    public void init() {
        Log.d("DisplayQuestionActivityTest", "Started test");
        Intents.init();
        Utils.waitAndTag(1000, "DisplayQuestionActivityTest");
        Player.get().resetPlayer();
        Player.get().addAnsweredQuestion(testQuestion.getQuestionId(), true, 0);
        Firestore.get().updateRemotePlayerDataFromLocal();

        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void cleanUp(){
        Log.d("DisplayQuestionActivityTest", "Finished test");
        mActivityRule.finishActivity();
        Intents.release();
        Utils.waitAndTag(1000, "DisplayQuestionActivityTest");
        Player.get().resetPlayer();
    }

    @Test
    public void correctAnswerGivesXpTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), testQuestion);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.answer_button)).perform(click());
        onView(withId(R.id.answer1)).perform(click());
        Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
    }

    @Test
    public void closeButtonTest() {
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), testQuestion);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.back_button)).perform(click());
        TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
