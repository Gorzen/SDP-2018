package ch.epfl.sweng.studyup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED_EDIT_QUESTION;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AAAEditQuestionActivityTest {
    private final String questionUUID = "Temporary fake uuid";
    private Question q;
    private ListView list;

    @Rule
    public final ActivityTestRule<QuestsActivityTeacher> mActivityRule =
            new ActivityTestRule<>(QuestsActivityTeacher.class);


    @Before
    public void init() {
        Player.get().setRole(Constants.Role.teacher);
        MOCK_ENABLED_EDIT_QUESTION = true;
    }

    @After
    public void disableMock() {
        Player.get().resetPlayer();
        MOCK_ENABLED_EDIT_QUESTION = false;
    }

    @Test
    public void editTrueFalseQuestionAnswer0to1Test() {
        q = new Question(questionUUID, "True false test", true, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
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
