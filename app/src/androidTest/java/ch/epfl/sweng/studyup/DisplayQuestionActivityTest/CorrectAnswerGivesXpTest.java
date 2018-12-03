package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.not;

//@RunWith(AndroidJUnit4.class)
public class CorrectAnswerGivesXpTest extends DisplayQuestionActivityTest {
    //@Test
    public void correctAnswerGivesXpTest(){
        Question testQuestion = new Question("abc", "test", true, 0, Constants.Course.SWENG.name(), "en");
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), testQuestion);
        mActivityRule.launchActivity(i);
        int playerXp = Player.get().getExperience();
        onView(withId(R.id.answer_button)).perform(click());
        onView(withId(R.id.answer2)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.answer4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.answer1)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.answer_button)).perform(click());
        waitAndTag(1500, this.getClass().getName());
        int expectedXP = Player.get().getExperience();
        int actualXP = playerXp + Constants.XP_GAINED_WITH_QUESTION;
        assertEquals(expectedXP, actualXP);
        Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
    }


}
