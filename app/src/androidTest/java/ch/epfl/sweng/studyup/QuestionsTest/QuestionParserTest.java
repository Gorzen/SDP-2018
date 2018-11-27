package ch.epfl.sweng.studyup.QuestionsTest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.AddOrEditQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static org.junit.Assert.assertArrayEquals;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class QuestionParserTest {

    @Rule
    public final ActivityTestRule<AddOrEditQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddOrEditQuestionActivity.class);

    @BeforeClass
    public static void runOnceBeforeClass() {
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }

    @Before
    public void init(){
        //delete all entries in the database
        QuestionDatabase.get(mActivityRule.getActivity()).clearAllTables();
    }

    @Test
    public void writeAndReadGivesTheCorrectList() {
        Question q1 = new Question("1", "test1", true, 0, Course.SWENG.name(), "en");
        Question q2 = new Question("2", "test2", false, 2, Course.SWENG.name(), "en");
        final ArrayList<Question> list = new ArrayList<>();
        list.add(q1);
        list.add(q2);
        QuestionParser.writeQuestions(list, mActivityRule.getActivity());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity());

        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                System.out.println(questions.toString());
                assertArrayEquals("parsed list should be the same as input", list.toArray(), questions.toArray());
            }
        });
    }

}
