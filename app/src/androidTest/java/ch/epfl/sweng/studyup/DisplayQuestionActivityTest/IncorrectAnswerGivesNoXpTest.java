package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class IncorrectAnswerGivesNoXpTest extends DisplayQuestionActivityTest {
    @Test
    public void incorrectAnswerGivesNoXpTest(){
        /*
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("abc", "test", false, 3));
        mActivityRule.launchActivity(i);
        int playerXp = Player.get().getExperience();
        onView(withId(R.id.answer2)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer1)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.answer4)).check(matches(isDisplayed())).perform(ViewActions.click());
        onView(withId(R.id.answer3)).check(matches(isDisplayed())).perform(ViewActions.click());
        onView(withId(R.id.answer_button)).perform(ViewActions.click());
        assertEquals(playerXp, Player.get().getExperience());
        Intents.intending(hasComponent(MainActivity.class.getName()));
        */
    }
}
