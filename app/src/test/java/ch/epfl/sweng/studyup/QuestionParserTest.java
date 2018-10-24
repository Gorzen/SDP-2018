package ch.epfl.sweng.studyup;

import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static org.junit.Assert.assertArrayEquals;

@RunWith(RobolectricTestRunner.class)
public class QuestionParserTest {

    private Context context;

    @Before
    public void init() {
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void writeAndReadGivesTheCorrectList() {
        Uri u1 = Uri.fromFile(new File("test1"));
        Question q1 = new Question(u1, true, 0);
        Uri u2 = Uri.fromFile(new File("test2"));
        Question q2 = new Question(u2, false, 2);
        ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        list.add(q2);
        QuestionParser.writeQuestions(list, context);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Question> parsedList = QuestionParser.parseQuestions(context);
        ArrayList<Question> newList = new ArrayList<>(parsedList);
        assertArrayEquals("parsed list should be the same as input", list.toArray(), newList.toArray());
    }

}
