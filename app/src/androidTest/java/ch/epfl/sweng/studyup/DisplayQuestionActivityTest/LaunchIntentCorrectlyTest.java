package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;

@RunWith(AndroidJUnit4.class)
public class LaunchIntentCorrectlyTest extends DisplayQuestionActivityTest {
    @Test
    public void launchIntentCorrectlyTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("abc", "test", true, 0));
        mActivityRule.launchActivity(i);
        Intents.intended(hasComponent(DisplayQuestionActivity.class.getName()));
    }
}
