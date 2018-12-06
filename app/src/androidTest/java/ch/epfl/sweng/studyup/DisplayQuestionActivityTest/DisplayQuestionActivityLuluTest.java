package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_DURATION;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ID;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_LANG;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TRUE_FALSE;
import static ch.epfl.sweng.studyup.utils.Constants.Course.SWENG;
import static ch.epfl.sweng.studyup.utils.Constants.Role.student;
import static ch.epfl.sweng.studyup.utils.Constants.XP_GAINED_WITH_QUESTION;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.junit.Assert.assertEquals;

public class DisplayQuestionActivityLuluTest {
    private String id = "ID Test display question";
    private Question q = new Question(id, "Test display question Lulu", false, 0, SWENG.name(), "en");

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class, true, false);

    @BeforeClass
    public static void enableMock(){
        MOCK_ENABLED = true;
    }

    @AfterClass
    public static void disableMock(){
        MOCK_ENABLED = false;
    }

    @Before
    public void resetPlayer(){
        Player.get().resetPlayer();
        Player.get().setRole(student);

        Intent intent = new Intent();
        intent.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        intent.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        intent.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        intent.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));
        intent.putExtra(DISPLAY_QUESTION_LANG, q.getLang());
        intent.putExtra(DISPLAY_QUESTION_DURATION, Long.toString(q.getDuration()));

        Player.get().addClickedInstant(id, System.currentTimeMillis());

        mActivityRule.launchActivity(intent);
    }

    @Test
    public void correctAnswerQuestionTest(){
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
    public void wrongAnswerQuestionTest(){
        int exp = Player.get().getExperience();

        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());

        assertEquals(exp, Player.get().getExperience());
    }
}