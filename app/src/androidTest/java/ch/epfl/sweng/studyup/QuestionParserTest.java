package ch.epfl.sweng.studyup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static org.junit.Assert.assertArrayEquals;

@RunWith(AndroidJUnit4.class)
public class QuestionParserTest {

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class);

    @Before
    public void init(){
        //delete all entries in the database
        QuestionDatabase.get(mActivityRule.getActivity()).clearAllTables();
    }

    @Test
    public void writeAndReadGivesTheCorrectList() {
        Uri u1 = Uri.fromFile(new File("test1"));
        Question q1 = new Question(u1, true, 0);
        Uri u2 = Uri.fromFile(new File("test2"));
        Question q2 = new Question(u2, false, 2);
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
                assertArrayEquals("parsed list should be the same as input", list.toArray(), questions.toArray());
            }
        });
    }

}
