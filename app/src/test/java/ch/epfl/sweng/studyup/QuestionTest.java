package ch.epfl.sweng.studyup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class QuestionTest {

    private Question q;
    String testCourseName = Constants.Course.SWENG.name();

    @Before
    public void init() {
        q = new Question("1", "question TEST", true, 0, testCourseName);
    }

    @Test
    public void gettersTest() {
        assertEquals(0, q.getAnswer());
        assertEquals(true, q.isTrueFalse());
        assertEquals("1", q.getQuestionId());
        assertEquals("question TEST", q.getTitle());

    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberHighTest() {
        Question q = new Question("1", "test", true, 2, testCourseName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberNegTest() {
        Question q = new Question("1", "test", true, -1, testCourseName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberMCQTest() {
        Question q = new Question("1", "test", false, 4, testCourseName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTitleTest() {
        Question q = new Question("1", null, true, 0, testCourseName);
    }

    @Test
    public void falseEqualsTest() {
        Question q = new Question("1", "test", true, 0, testCourseName);
        assertFalse(q.equals(null));
    }

    @Test
    public void correctEqualsTest(){
        assertTrue(q.equals(new Question("1", "question TEST", true, 0, testCourseName)));
    }


    @Test
    public void settersTest() {
        q.setAnswer(1);
        assertEquals(q.getAnswer(), 1);
        q.setAnswer(2);
        assertEquals(1, q.getAnswer());
        q.setAnswer(-1);
        assertEquals(1, q.getAnswer());
        q.setTrueFalse(false);
        assertEquals(false, q.isTrueFalse());
        q.setAnswer(2);
        assertEquals(2, q.getAnswer());
        q.setAnswer(3);
        assertEquals(3, q.getAnswer());
        q.setAnswer(4);
        assertEquals(3, q.getAnswer());
        q.setTitle("new title");
        assertEquals("new title", q.getTitle());
        q.setQuestionId("2");
        assertEquals("2", q.getQuestionId());
    }
}