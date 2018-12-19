
package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.anything;
public class FromStudentToDisplayTest {
    @Rule
    public final ActivityTestRule<QuestsActivityStudent> rule =
            new ActivityTestRule<>(QuestsActivityStudent.class, true, false);
    private final String TAG = FromStudentToDisplayTest.class.getSimpleName();
    private Question q;
    @SuppressWarnings("HardCodedStringLiteral")
    private final String fakeUUID = "fake-UUID";
    private final String fakeTitle = "fake title from student to display";

    @Before
    public void addQuestionThatWillBeDisplayed() {
        Player.get().resetPlayer();
        q = new Question(fakeUUID , fakeTitle, false, 3, Constants.Course.SWENG.name(), "en");
        Firestore.get().addQuestion(q);
        waitAndTag(1000, TAG);
        Player.get().setRole(Constants.Role.teacher);
        rule.launchActivity(new Intent());
        waitAndTag(1000, TAG);
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @After
    public void stopMock() {
        GlobalAccessVariables.MOCK_ENABLED = false;
    }

    @Test
    public void listViewRedirectOnCorrectQuestion() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(i)
                        .perform(click());
                waitAndTag(2000, TAG);

                onView(withId(R.id.answer1)).perform(click());
                onView(withId(R.id.answer2)).perform(click());
                onView(withId(R.id.answer3)).perform(click());
                onView(withId(R.id.answer4)).perform(click());
                onView(withId(R.id.answer_button)).perform(click());
            }
        }
    }

    @Test
    public void listViewRedirectOnCorrectQuestionWithFalseAnswer() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(i)
                        .perform(click());
                waitAndTag(2000, TAG);
                int xpBeforeAnswer = Player.get().getExperience();
                onView(withId(R.id.answer1)).perform(click());
                onView(withId(R.id.answer_button)).perform(click());
                assertEquals(xpBeforeAnswer, Player.get().getExperience());
            }
        }
    }
}