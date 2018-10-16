package ch.epfl.sweng.studyup;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import java.io.File;

import ch.epfl.sweng.studyup.question.Question;

@RunWith(RobolectricTestRunner.class)
public class QuestionTest {

    private Uri uri;

    @Before
    public void init() {
        uri = Uri.fromFile(new File("test.test"));
    }

    @Test
    public void gettersAndSettersTest() {
        Question q = new Question(uri, true, 1);
        assertEquals(q.getAnswer(),1);
        assertEquals(q.isTrueFalseQuestion(),true);
        assertEquals(q.getQuestionUri(), uri);

    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberHighTest() {
        Question q = new Question(uri, true, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberNegTest() {
        Question q = new Question(uri, true, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongAnswerNumberMCQTest() {
        Question q = new Question(uri, false, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUriTest() {
        Question q = new Question(null, true, 0);
    }

    @Test
    public void falseEqualsTest(){
        Question q = new Question(uri, true, 0);
        assertFalse(q.equals(null));
    }
}