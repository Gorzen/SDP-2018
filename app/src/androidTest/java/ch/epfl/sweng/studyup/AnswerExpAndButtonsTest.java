package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;

@RunWith(AndroidJUnit4.class)
public class AnswerExpAndButtonsTest {
    private static final String TAG = "DisplayQuestionActivityTest2";

    @Rule
    public final ActivityTestRule<DisplayQuestionActivity> mActivityRule =
            new ActivityTestRule<DisplayQuestionActivity>(DisplayQuestionActivity.class, false, true){
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    MOCK_ENABLED = true;
                }

                @Override
                protected void afterActivityFinished() {
                    super.afterActivityFinished();
                    MOCK_ENABLED = false;
                }
            };

    @Test
    public void answerExpAndButtonsTest() {
        /*
        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());
        */
        /*
        int exp = Player.get().getExperience();

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
