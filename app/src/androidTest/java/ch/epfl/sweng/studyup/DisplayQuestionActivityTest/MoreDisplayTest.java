package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ID;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class MoreDisplayTest extends DisplayQuestionActivityTest {
    @Test
    public void launchIntentWithoutUriTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("abc","test", true, 0, Constants.Course.SWENG.name()));
        i.removeExtra(DISPLAY_QUESTION_ID);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void launchCorrectIntentTrueFalseTest() {
        Question q = new Question("id-test", "Test title", true, 0, Constants.Course.SWENG.name());
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), q);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.answer1)).check(matches(isDisplayed()));
        onView(withId(R.id.answer2)).check(matches(isDisplayed()));
        onView(withId(R.id.answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.answer4)).check(matches(not(isDisplayed())));
    }

    @Test
    public void launchCorrectIntentMCQTest() {
        Question q = new Question("id-test", "Test title", false, 0, Constants.Course.SWENG.name());
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), q);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.answer1)).check(matches(isDisplayed()));
        onView(withId(R.id.answer2)).check(matches(isDisplayed()));
        onView(withId(R.id.answer3)).check(matches(isDisplayed()));
        onView(withId(R.id.answer4)).check(matches(isDisplayed()));
        Utils.waitAndTag(1000, TAG);
        onView(withId(R.id.questionProgressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.question_display_view)).check(matches(isDisplayed()));
        onView(withId(R.id.question_text_display)).check(matches(not(isDisplayed())));
    }
}