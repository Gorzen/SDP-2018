package ch.epfl.sweng.studyup.QuestionsTest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;
import okhttp3.internal.Util;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class QuestsActivityTeacherTest {
    @Rule
    public final ActivityTestRule<QuestsActivityTeacher> rule =
            new ActivityTestRule<>(QuestsActivityTeacher.class, true, false);
    private final String TAG = QuestsActivityTeacher.class.getSimpleName();
    private Question q;
    private  final String fakeTitle = "fake title";

    @BeforeClass
    public static void enableMock() {
        Player.get().initializeDefaultPlayerData();
        Player.get().setRole(Role.teacher);
    }

    @Before
    public void addQuestionThatWillBeDisplayed() {
        q = new Question(MOCK_UUID, fakeTitle, true, 0, Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(500, TAG);
        rule.launchActivity(new Intent());
        Utils.waitAndTag(100, TAG);
    }

    @Test
    public void listViewDisplayCorrectQuestion() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        for (int i = 0; i < list.getAdapter().getCount(); ++i) {
            Question currQuestion = (Question) list.getAdapter().getItem(i);
            if (currQuestion.getTitle().equals(fakeTitle)) {
                assertEquals(q.isTrueFalse(), currQuestion.isTrueFalse());
                assertEquals(q.getAnswer(), currQuestion.getAnswer());
                assertEquals(q.getCourseName(), currQuestion.getCourseName());

            }
        }
    }

    @Test
    public void shouldHaveAtLeastOneQuestion() {
        final ListView list = rule.getActivity().findViewById(R.id.listViewQuests);
        assert (1 <= list.getAdapter().getCount());
    }

    @Test
    public void canCancelDeletionOfQuest() {
        Utils.waitAndTag(150, TAG);

        /*
            Other workaround, just in case

        onView(allOf(
        withId(R.id.delete_question),
        nthChildsDescendant(withId(R.id.listViewQuests), 0)))
        .perform(click());*/


        onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                .atPosition(0)
                .onChildView(withId(R.id.delete_question))
                .perform(click());
        Utils.waitAndTag(2000, TAG);
        onView(withText(R.string.no_upper)).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Utils.waitAndTag(2000, TAG);

        //should be able to click on bottom bar at this point
        BottomNavigationView b = rule.getActivity().findViewById(R.id.bottomNavView_Bar);
        b.setSelectedItemId(R.id.navigation_add_question);
    }

    @Test
    public void canDeleteQuestionUsingButton() {
        deleteAllQuestsByUsingButton();

        Firestore.get().loadQuestions(rule.getActivity());
        Utils.waitAndTag(2000, TAG);
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(rule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(rule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                for(Question q : questions) {
                    assertFalse(q.getTitle() == fakeTitle);
                }
            }
        });
    }

    private void deleteAllQuestsByUsingButton() {
        try {
            while(true) {
                /*
                Other workaround, just in case
                onView(allOf(
                    withId(R.id.delete_question),
                    nthChildsDescendant(withId(R.id.listViewQuests), 0)))
                    .perform(click());
                */


                onData(anything()).inAdapterView(withId(R.id.listViewQuests))
                        .atPosition(0)
                        .onChildView(withId(R.id.delete_question))
                        .perform(click());
                Utils.waitAndTag(2000, TAG);
                onView(withText(R.string.yes_upper)).inRoot(isDialog())
                        .check(matches(isDisplayed()))
                        .perform(click());
                Utils.waitAndTag(2000, TAG);
            }
        } catch (Exception e) {}
    }

    // Method not used, can be useful so keep it just in case. Credit: https://stackoverflow.com/questions/32823508/how-can-i-click-on-a-view-in-listview-specific-row-position#
    private static Matcher<View> nthChildsDescendant(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {

                while(view.getParent() != null) {
                    if(parentMatcher.matches(view.getParent())) {
                        return view.equals(((ViewGroup) view.getParent()).getChildAt(childPosition));
                    }
                    view = (View) view.getParent();
                }

                return false;
            }
        };
    }
}