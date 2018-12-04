package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.anything;

@SuppressWarnings("HardCodedStringLiteral")
public class CannotAnswerTwiceTest {

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
        Player.get().addAnsweredQuestion(q.getQuestionId(), true, 0);
        Player.get().setRole(Constants.Role.student);
        Firestore.get().updateRemotePlayerDataFromLocal();
        rule.launchActivity(new Intent());
        waitAndTag(1000, TAG);
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @After
    public void stopMock() {
        GlobalAccessVariables.MOCK_ENABLED = false;
    }


    @Test
    public void cannotAnswerTwiceTest() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(i)
                        .perform(click());
                waitAndTag(4000, TAG);

                int playerXp = Player.get().getExperience();
                onView(withId(R.id.answer1)).perform(click()).check(matches(isChecked()));
                onView(withId(R.id.answer_button)).perform(click());
                waitAndTag(1500, this.getClass().getName());
                int expectedXP = Player.get().getExperience();
                assertEquals(expectedXP, playerXp);
                Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
            }
        }
    }
}
