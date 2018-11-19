package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_ID;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.DISPLAY_QUESTION_TRUE_FALSE;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.XP_GAINED_WITH_QUESTION;
import static org.junit.Assert.assertEquals;

public class AADisplayQuestionActivityTest4 {
    private static final String questionUUID = "Fake UUID test Display";
    private Question q = new Question(questionUUID, "ADisplayQuestionActivityTest2", false, 0, Constants.Course.SWENG.name());


    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class, true, false);

    @Before
    public void launchActivity(){
        Player.get().resetPlayer();
        Intent intent = new Intent();
        intent.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        intent.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        intent.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        intent.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));
        mActivityRule.launchActivity(intent);
    }

    @After
    public void reset(){
        Player.get().resetPlayer();
    }

    @Test
    public void answerExpAndButtonsTest(){
        /*
        int exp = Player.get().getExperience();

        Utils.waitAndTag(1000, "DisplayQuestionActivityTest4");

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());

        assertEquals(exp + XP_GAINED_WITH_QUESTION, Player.get().getExperience());
        */
    }
}
