package ch.epfl.sweng.studyup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AAAEditQuestionActivityTest {
    private final String questionUUID = "Temporary fake uuid";
    private Question q;
    private ListView list;

    @Rule
    public final ActivityTestRule<QuestsActivityTeacher> mActivityRule =
            new ActivityTestRule<>(QuestsActivityTeacher.class, true, false);


    @Before
    public void init() {
        Player.get().setRole(Constants.Role.teacher);
    }

    @Test
    public void editTrueFalseQuestionAnswer0to1Test() {
        q = new Question(questionUUID, "True false test", true, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(1, questions.get(0).getAnswer());
                    assertEquals(true, questions.get(0).isTrueFalse());
                }
            }
        });
    }

    private void clickOnListViewItem() {
        mActivityRule.launchActivity(new Intent());
        list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
    }
}
