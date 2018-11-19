package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
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
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AADisplayQuestionActivityTest3 {
    private static final String questionUUID = "Fake UUID test Display";
    Question q = new Question(questionUUID, "ADisplayQuestionActivityTest2", false, 0, Constants.Course.SWENG.name());
    private Intent intent = new Intent();
    private Intent intent2 = null;

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<>(DisplayQuestionActivity.class);

    @BeforeClass
    public static void changeSciper(){
        Player.get().setSciperNum("100001");
    }

    @AfterClass
    public static void deleteQuestion(){
        Firestore.get().deleteQuestion(questionUUID);
        Player.get().resetPlayer();
    }

    @Test
    public void AddQuestion(){
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");
        Player.get().setRole(Constants.Role.teacher);
    }

    @Test
    public void aLoadQuestions(){
        Firestore.get().loadQuestions(mActivityRule.getActivity().getApplicationContext());
        Utils.waitAndTag(3000, "DisplayQuestionActivityTest2");

        Intent goToQuestion = new Intent();
        goToQuestion.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        goToQuestion.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        goToQuestion.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        goToQuestion.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));

        intent2 = goToQuestion;
    }

    @Test
    public void displayQuestionTestIntent(){
        mActivityRule.launchActivity(getIntentForDisplayQuestion(mActivityRule.getActivity().getApplicationContext(), q));

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());
    }
}
