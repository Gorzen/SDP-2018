package ch.epfl.sweng.studyup;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

import ch.epfl.sweng.studyup.question.Question;


public class QuestionTest {

    private Uri uri;

    @Before
    private void init() {
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
    private void wrongAnswerNumberHighTest() {
        Question q = new Question(uri, true, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    private void wrongAnswerNumberNegTest() {
        Question q = new Question(uri, true, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    private void wrongAnswerNumberMCQTest() {
        Question q = new Question(uri, false, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    private void nullUriTest() {
        Question q = new Question(null, true, 0);
    }

}