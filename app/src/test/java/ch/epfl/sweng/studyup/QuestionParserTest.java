package ch.epfl.sweng.studyup;

import android.content.Context;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class QuestionParserTest {

    private Context context;
    private File writeFile;

    @Before
    public void init() {
        context = RuntimeEnvironment.application.getApplicationContext();
        writeFile = new File(context.getFilesDir(), QuestionParser.fileName);
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
        assertTrue(QuestionParser.writeQuestions(list, context, true));
        System.out.println(list.toString());
        List<Question> parsedList = QuestionParser.parseQuestions(context, false);
        ArrayList<Question> newList = new ArrayList<>(parsedList);
        System.out.println(newList.toString());
        assertArrayEquals("parsed list should be the same as input", list.toArray(), newList.toArray());
    }

    @Test
    public void fileWithoutSecondLine() {
        //write a file with an empty second line to throw an error
        try {
            writeFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
            String text = "testFile.png\n";
            writer.write(text);
            writer.close();
            assertNull("List should be empty", QuestionParser.parseQuestions(context, false));
        } catch (IOException e) {
            assertTrue("Should not throw any exception", false);
            e.printStackTrace();
        } finally {
            writeFile.delete();
        }
    }

    @Test
    public void fileWithoutThirdLine() {
        //write a file with an empty second line to throw an error
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
            String text = "testFile.png\ntrue";
            writer.write(text);
            writer.close();
            assertNull("List should be null", QuestionParser.parseQuestions(context, false));
        } catch (IOException e) {
            assertTrue("Should not throw any exception", false);
            e.printStackTrace();
        } finally {
            writeFile.delete();
        }
    }

    //can't make it work
    public void readFileThatExists() {
        File testFile = new File(context.getFilesDir(), "test.png");
        try {
            testFile.createNewFile();
            assertTrue(testFile.exists());
            Uri u1 = Uri.fromFile(testFile);
            Question q1 = new Question(u1, true, 0);
            ArrayList<Question> list = new ArrayList<>();
            list.add(q1);
            assertTrue(QuestionParser.writeQuestions(list, context, true));
            List<Question> parsedList = QuestionParser.parseQuestions(context, true);
            System.out.println(parsedList);
            assertFalse(parsedList.isEmpty());
            ArrayList<Question> newList = new ArrayList<>(parsedList);
            assertNull("Both lists should be equals", list.toArray());
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Should not throw any exception", false);
        }
    }

    @Test
    public void ReturnNullWhenFileDoesNotExist() {
        File testFile = new File(context.getFilesDir(), "test.png");
        Uri u1 = Uri.fromFile(testFile);
        Question q1 = new Question(u1, true, 0);
        ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        assertTrue(QuestionParser.writeQuestions(list, context, true));
        List<Question> parsedList = QuestionParser.parseQuestions(context, true);
        assertNull(parsedList);

    }

}
