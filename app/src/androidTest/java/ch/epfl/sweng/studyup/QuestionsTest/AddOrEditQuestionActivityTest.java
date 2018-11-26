package ch.epfl.sweng.studyup.QuestionsTest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddOrEditQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(AndroidJUnit4.class)
public class AddOrEditQuestionActivityTest {
    private static final String TAG = AddOrEditQuestionActivityTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<AddOrEditQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddOrEditQuestionActivity.class, true, false);

    @BeforeClass
    public static void enableMock() {
        MOCK_ENABLED = true;
        Intents.init();
    }

    @AfterClass
    public static void disableMock() {
        MOCK_ENABLED = false;
        Intents.release();
    }

    @Before
    public void initiateIntents() {
        mActivityRule.launchActivity(new Intent());
        QuestionDatabase.get(mActivityRule.getActivity()).clearAllTables();
        closeSoftKeyboard();
    }


    //@Test todo
    public void testCheckOfTrueFalse() {
        onView(ViewMatchers.withId(R.id.true_false_radio)).perform(scrollTo(), click());
        onView(withId(R.id.mcq_radio)).perform(scrollTo(), click());
        onView(withId(R.id.true_false_radio)).perform(scrollTo(), click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(scrollTo(), click()).check(matches(isChecked())).check(matches(withText(R.string.truth_value)));
        onView(withId(R.id.radio_answer2)).perform(scrollTo(), click()).check(matches(isChecked())).check(matches(withText(R.string.false_value)));
        onView(withId(R.id.radio_answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.radio_answer4)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testCheckOfMCQ() {
        onView(withId(R.id.mcq_radio)).perform(scrollTo(), click());
        onView(withId(R.id.radio_answer4)).perform(scrollTo(), click());
        onView(withId(R.id.radio_answer3)).perform(scrollTo(), click());
        onView(withId(R.id.radio_answer2)).perform(scrollTo(), click());
        onView(withId(R.id.radio_answer1)).perform(scrollTo(), click()).check(matches(isChecked()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFalseInstanceQuestion() {
        Question nullQ = new Question("1", null, false, 0, Course.SWENG.name(), "en");
    }

    @Test
    public void testSimpleInstanceQuestionTrueFalse() {
        Question simple = new Question("1", "test2134", true, 0, Course.SWENG.name(), "en");
        assert (simple.isTrueFalse());
        assert (simple.getAnswer() == 0);
        assert (simple.getQuestionId().equals("1"));
        assert (simple.getTitle().equals("test2134"));
    }

    @Test
    public void testSimpleInstanceQuestionMCQ() {
        Question simple = new Question("4", "test", false, 2, Course.SWENG.name(), "en");
        assert (!simple.isTrueFalse());
        assert (simple.getAnswer() == 2);
        assert (simple.getTitle().equals("test"));
        assert (simple.getQuestionId().equals("4"));
    }

    @Test
    public void activityResultTest() {
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(scrollTo()).perform(click());
    }


    @Test
    public void addQuestionTest() throws Throwable {
        //Question: MCQ, answer: 0, course: SWENG
        List<Course> courses = Arrays.asList(Course.SWENG, Course.Algebra);
        Player.get().setCourses(courses);
        onView(withId(R.id.choice_course_button)).perform(scrollTo()).perform(click());
        onView(withText("Software Engineering")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(scrollTo()).perform(click());
        onView(ViewMatchers.withId(R.id.radio_answer1)).perform(scrollTo()).perform(click());
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(scrollTo()).perform(click());

        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText title = mActivityRule.getActivity().findViewById(R.id.questionTitle);
                title.setText("A Title");
            }
        });

        final ScrollView scroll = mActivityRule.getActivity().findViewById(R.id.scrollViewAddQuestion);
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
        Utils.waitAndTag(500, "Waiting for scroll");

        onView(ViewMatchers.withId(R.id.addQuestionButton)).perform(scrollTo(), click());
        Utils.waitAndTag(500, TAG);
        Player.get().setRole(Role.teacher);
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        Utils.waitAndTag(500, TAG);

        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    //assertEquals(0, questions.get(0).getAnswer());
                    assertFalse(questions.get(0).isTrueFalse());
                    assertEquals(Course.SWENG.name(), questions.get(0).getCourseName());
                }
            }
        });
        Utils.waitAndTag(100, TAG);
    }
}