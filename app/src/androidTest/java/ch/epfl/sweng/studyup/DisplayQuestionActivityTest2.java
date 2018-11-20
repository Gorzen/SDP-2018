package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.XP_GAINED_WITH_QUESTION;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DisplayQuestionActivityTest2 {
    private static final String TAG = "DisplayQuestionActivityTest2";

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class);

    @BeforeClass
    public static void enableMock(){
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void disableMock(){
        MOCK_ENABLED = false;
    }

    @Before
    public void resetPlayerBefore(){
        Player.get().resetPlayer();
    }

    @After
    public void resetPlayerAfter(){
        Player.get().resetPlayer();
    }

    @Test
    public void answerExpAndButtonsTest() {
        int exp = Player.get().getExperience();

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());

        assertEquals(exp + XP_GAINED_WITH_QUESTION, Player.get().getExperience());
    }

    @Test
    public void wrongAnswerTest() {
        int exp = Player.get().getExperience();

        onView(withId(R.id.answer2)).perform(click());

        onView(withId(R.id.answer_button)).perform(click());

        assertEquals(exp, Player.get().getExperience());
    }
}
