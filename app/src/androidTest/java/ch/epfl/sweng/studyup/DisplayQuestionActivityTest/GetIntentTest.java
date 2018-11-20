package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ID;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TRUE_FALSE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GetIntentTest extends DisplayQuestionActivityTest {
    @Test
    public void getIntentTest() {
        Question q = new Question("abc", "test1", true, 0, Course.SWENG.name(), "en");
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
}
