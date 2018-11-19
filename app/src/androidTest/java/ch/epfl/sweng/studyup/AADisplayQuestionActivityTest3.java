package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.XP_GAINED_WITH_QUESTION;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AADisplayQuestionActivityTest3 {
    private static final String questionUUID = "Fake UUID test Display";
    List<Question> question = new ArrayList<Question>(){
        {
            add(new Question(questionUUID, "ADisplayQuestionActivityTest2", false, 0, Constants.Course.SWENG.name()));
        }
    };
    private ListView list;
    private View view;
    private long id;

    @Rule
    public final ActivityTestRule<QuestsActivityStudent> mActivityRule =
            new ActivityTestRule<>(QuestsActivityStudent.class);

    @BeforeClass
    public static void enableMock(){
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void resetPlayer(){
        Player.get().resetPlayer();
        MOCK_ENABLED = false;
    }

    @Test
    public void answerQuestionButtonsExpTest() {
        int exp = Player.get().getExperience();

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);

                mActivityRule.getActivity().setupListView(question);

                list.performItemClick(list.getAdapter().getView(0, null, null), 0, list.getAdapter().getItemId(0));
            }
        });

        Utils.waitAndTag(10000, "DisplayQuestionActivityTest2");

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());

        assertEquals(exp + XP_GAINED_WITH_QUESTION, Player.get().getExperience());
    }
}
