package ch.epfl.sweng.studyup;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestNQuestions;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(JUnit4.class)
public class SpecialQuestNQuestionsTest {

    @BeforeClass
    public static void init() {
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @AfterClass
    public static void clean(){
        GlobalAccessVariables.MOCK_ENABLED = false;
    }

    @Test
    public void gettersSettersTest() {
        String title = "Test title";
        String description = "Test description";
        SpecialQuestNQuestions sq = new SpecialQuestNQuestions(title, description, 3, 3);
        assertEquals(title, sq.getTitle());
        assertEquals(description, sq.getDescription());
        assertEquals(3, sq.getGoal());
        assertEquals(Constants.SpecialQuestsType.NQUESTIONS, sq.getId());
        assertEquals(3, sq.getLevel());
        assertEquals(0.0, sq.getProgress());
        sq.reward();

        sq.setGoal(10);
        assertEquals(10, sq.getGoal());

        sq.setProgress(0.5);
        assertEquals(0.5, sq.getProgress());
        sq.setProgress(0.0);

        /*Update does work yet because of firebase
        Question q = new Question("1", "title", true, 0, "SWENG");
        sq.update(null, q);
        assertEquals(0.2, sq.getProgress());
        */
    }

    @Test
    public void equalsTrueTest() {
        SpecialQuestNQuestions sq = new SpecialQuestNQuestions("same title", "same desc", 3, 0);
        SpecialQuestNQuestions sq2 = new SpecialQuestNQuestions("same title", "same desc", 4, 5);
        assertTrue(sq.equals(sq2));
    }

    @Test
    public void equalsFalseTest() {
        SpecialQuestNQuestions sq = new SpecialQuestNQuestions("diff title", "same desc", 3, 0);
        SpecialQuestNQuestions sq2 = new SpecialQuestNQuestions("same title", "same desc", 4, 5);
        assertFalse(sq.equals(sq2));

        assertFalse(sq.equals(new ArrayList<String>()));
    }
}
