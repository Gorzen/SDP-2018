package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TRUE_FALSE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LaunchIntentWithoutTrueFalseTest extends DisplayQuestionActivityTest {
    @Test
    public void launchIntentWithoutTrueFalseTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("abc", "test", true, 0));
        i.removeExtra(DISPLAY_QUESTION_TRUE_FALSE);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }
}
