package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ID;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MoreDisplayTest extends DisplayQuestionActivityTest {
    @Test
    public void launchIntentWithoutUriTest(){
        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), new Question("abc","test", true, 0, Constants.Course.SWENG.name()));
        i.removeExtra(DISPLAY_QUESTION_ID);
        mActivityRule.launchActivity(i);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }


}
