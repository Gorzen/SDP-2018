package ch.epfl.sweng.studyup.QuestionsTest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDAO;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ch.epfl.sweng.studyup.utils.Constants.Course;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class QuestionDatabaseTest {
    private QuestionDAO questionDAO;
    private QuestionDatabase database;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        database = Room.inMemoryDatabaseBuilder(context, QuestionDatabase.class).build();
        questionDAO = database.questionDAO();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void writeSingleQuestionTest() {
        Question q = new Question("1", "test", true , 0,  Course.SWENG.name(), "en");
        ArrayList<Question> list = new ArrayList<>();
        list.add(q);
        questionDAO.insertAll(list);
        ArrayList<Question> databaseList = new ArrayList<>(questionDAO.getAll());
        assertThat(databaseList, equalTo(list));
    }

    @Test
    public void writeMultipleQuestionsTest(){
        Question q1 = new Question("1", "test1", true , 0, Course.SWENG.name(), "en");
        Question q2 = new Question("2", "test2", true , 0, Course.SWENG.name(), "en");
        ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        list.add(q2);
        questionDAO.insertAll(list);
        ArrayList<Question> databaseList = new ArrayList<>(questionDAO.getAll());
        assertThat(databaseList, equalTo(list));
    }
}

