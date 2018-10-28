package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.android.gms.common.util.IOUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.*;
import ch.epfl.sweng.studyup.questions.Question;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DisplayQuestionActivityTest {

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class,
                    true,
                    false);

    @Before
    public void initiateIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void getIntentTest(){
        Question q = new Question("1", "test1", true, 0);
        Intent testIntent = getIntentForDisplayQuestion(InstrumentationRegistry.getContext(), q);
        assertTrue(testIntent.hasExtra(DISPLAY_QUESTION_TRUE_FALSE));
        assertTrue(testIntent.hasExtra(DISPLAY_QUESTION_ANSWER));
        assertTrue(testIntent.hasExtra(DISPLAY_QUESTION_TITLE));
        assertTrue(testIntent.hasExtra(DISPLAY_QUESTION_ID));
        assertEquals(testIntent.getStringExtra(DISPLAY_QUESTION_TITLE), q.getTitle());
        assertEquals(testIntent.getStringExtra(DISPLAY_QUESTION_ID), q.getQuestionId());
        assertEquals(testIntent.getStringExtra(DISPLAY_QUESTION_TRUE_FALSE), Boolean.toString(q.isTrueFalse()));
        assertEquals(testIntent.getStringExtra(DISPLAY_QUESTION_ANSWER), Integer.toString(q.getAnswer()));
    }

    @Test
    public void launchIntentWithoutTitleTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1","test", true, 0));
        i.removeExtra(DISPLAY_QUESTION_TITLE);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void launchIntentWithoutUriTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1","test", true, 0));
        i.removeExtra(DISPLAY_QUESTION_TITLE);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void launchIntentWithoutAnswerTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1","test", true, 0));
        i.removeExtra(DISPLAY_QUESTION_ANSWER);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void launchIntentWithoutTrueFalseTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1", "test", true, 0));
        i.removeExtra(DISPLAY_QUESTION_TRUE_FALSE);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void launchIntentCorrectlyTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1", "test", true, 0));
        mActivityRule.launchActivity(i);
        Intents.intended(hasComponent(DisplayQuestionActivity.class.getName()));
    }

    @Test
    public void CorrectAnswerGivesXpTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1", "test", true, 0));
        mActivityRule.launchActivity(i);
        int playerXp = Player.get().getExperience();
        onView(withId(R.id.answer2)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer1)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.answer4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.answer_button)).perform(ViewActions.click());
        assertEquals(playerXp + XP_GAINED_WITH_QUESTION, Player.get().getExperience());
        Intents.intending(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void IncorrectAnswerGivesNoXpTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1", "test", false, 3));
        mActivityRule.launchActivity(i);
        int playerXp = Player.get().getExperience();
        onView(withId(R.id.answer2)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer1)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer4)).check(matches(isDisplayed())).perform(ViewActions.click());
        onView(withId(R.id.answer3)).check(matches(isDisplayed())).perform(ViewActions.click());
        onView(withId(R.id.answer_button)).perform(ViewActions.click());
        assertEquals(playerXp, Player.get().getExperience());
        Intents.intending(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void backButton() {
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("1", "test", true, 0));
        mActivityRule.launchActivity(i);
        onView(withId(R.id.back_button)).perform(ViewActions.click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
