package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

//@RunWith(AndroidJUnit4.class)
@SuppressWarnings("HardCodedStringLiteral")
public class CorrectAnswerGivesXpTest extends DisplayQuestionActivityTest {
    @Rule
    public final ActivityTestRule<QuestsActivityStudent> rule =
            new ActivityTestRule<>(QuestsActivityStudent.class, true, false);
    private final String TAG = FromStudentToDisplayTest.class.getSimpleName();
    private Question q;
    private  final String fakeTitle = "fake title from student to display for text";
    private final String UUID = "UUID-for-text-question";

    @Before
    public void addQuestionThatWillBeDisplayed() {
        q = new Question(UUID, fakeTitle, true, 0, Constants.Course.SWENG.name(), "en");
        Firestore.get().addQuestion(q);
        waitAndTag(1000, TAG);
        Player.get().setRole(Constants.Role.student);
        rule.launchActivity(new Intent());
        waitAndTag(1000, TAG);
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @After
    public void stopMock() {
        GlobalAccessVariables.MOCK_ENABLED = false;
    }


    @Test
    public void correctAnswerGivesXpTest() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(i)
                        .perform(click());
                waitAndTag(4000, TAG);
                //noinspection HardCodedStringLiteral
                onView(withId(R.id.question_text_display)).check(matches(withText("Short text\n")));

                int playerXp = Player.get().getExperience();
                onView(withId(R.id.answer_button)).perform(click());
                onView(withId(R.id.answer2)).perform(click()).check(matches(isChecked()));
                onView(withId(R.id.answer3)).check(matches(not(isDisplayed())));
                onView(withId(R.id.answer4)).check(matches(not(isDisplayed())));
                onView(withId(R.id.answer1)).perform(click()).check(matches(isChecked()));
                onView(withId(R.id.answer_button)).perform(click());
                waitAndTag(1500, this.getClass().getName());
                int expectedXP = Player.get().getExperience();
                int actualXP = playerXp + Constants.XP_GAINED_WITH_QUESTION;
                assertEquals(expectedXP, actualXP);
                Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
            }
        }
    }

    @Test
    public void backButtonTest() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(i)
                        .perform(click());
                waitAndTag(4000, TAG);

                onView(withId(R.id.back_button)).perform(click());
                TestCase.assertTrue(mActivityRule.getActivity().isFinishing());
            }
        }
    }

}
