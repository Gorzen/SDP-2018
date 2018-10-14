package ch.epfl.sweng.studyup;

import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.question.Question;
import ch.epfl.sweng.studyup.question.QuestionParser;

@RunWith(RobolectricTestRunner.class)
public class QuestionParserTest {

    @Test
    public void writeAndReadGivesTheCorrectList(){
        Uri u1 = Uri.fromFile(new File("test1"));
        Question q1 =  new Question(u1, true, 0);
        Uri u2 =  Uri.fromFile(new File("test2"));
        Question q2 = new Question(u2, false, 2);
        ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        list.add(q2);
        Context c = RuntimeEnvironment.application.getApplicationContext();
        QuestionParser.writeQuestions(list, c, true);
        try {
            System.out.println(list.toString());
            List<Question> parsedList = QuestionParser.parseQuestions(c, false);
            ArrayList<Question> newList = new ArrayList<>(parsedList);
            System.out.println(newList.toString());
            assertArrayEquals("parsed list should be the same as input", list.toArray(), newList.toArray());
        }catch(FileNotFoundException e){
            assertTrue("No exception should occur", false);
        }
    }

}
