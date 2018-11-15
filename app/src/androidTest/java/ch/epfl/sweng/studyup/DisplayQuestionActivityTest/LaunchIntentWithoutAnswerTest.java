package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LaunchIntentWithoutAnswerTest extends DisplayQuestionActivityTest {
    @Test
    public void launchIntentWithoutAnswerTest() {
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(),
                new Question("abc", "test", true, 0, Course.SWENG.name()));
        i.removeExtra(DISPLAY_QUESTION_ANSWER);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
