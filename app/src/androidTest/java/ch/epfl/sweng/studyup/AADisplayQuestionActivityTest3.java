package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.XP_GAINED_WITH_QUESTION;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AADisplayQuestionActivityTest3 {
    private static final String questionUUID = "Fake UUID test Display";
    private Question q = new Question(questionUUID, "ADisplayQuestionActivityTest", false, 0, Constants.Course.SWENG.name());
    private final String TAG = getClass().getSimpleName();

    @Rule
    public final ActivityTestRule<QuestsActivityStudent> rule =
            new ActivityTestRule<>(QuestsActivityStudent.class, true, false);

    @BeforeClass
    public static void enableMock(){
        Player.get().resetPlayer();
        Player.get().setRole(Constants.Role.teacher);
    }

    @AfterClass
    public static void resetPlayer(){
        Player.get().resetPlayer();
    }

    @Before
    public void addQuestionThatWillBeDisplayed() {
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(500, TAG);
        rule.launchActivity(new Intent());
        Utils.waitAndTag(100, TAG);
        Firestore.get().loadQuestions(rule.getActivity().getApplicationContext());
        Utils.waitAndTag(2000, TAG);
    }

    @Test
    public void answerQuestionButtonsExpTest() {
        int exp = Player.get().getExperience();

        onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                .atPosition(0)
                .perform(click());
        Utils.waitAndTag(2000, TAG);

        onView(withId(R.id.answer1))
                .perform(click());
        Utils.waitAndTag(100, TAG);
        onView(withId(R.id.answer2))
                .perform(click());
        Utils.waitAndTag(100, TAG);
        onView(withId(R.id.answer3))
                .perform(click());
        Utils.waitAndTag(100, TAG);
        onView(withId(R.id.answer4))
                .perform(click());
        Utils.waitAndTag(100, TAG);

        onView(withId(R.id.answer1))
                .perform(click());
        Utils.waitAndTag(100, TAG);
        onView(withId(R.id.answer_button))
                .perform(click());
        Utils.waitAndTag(100, TAG);

        assertEquals(exp + XP_GAINED_WITH_QUESTION, Player.get().getExperience());
    }
}
