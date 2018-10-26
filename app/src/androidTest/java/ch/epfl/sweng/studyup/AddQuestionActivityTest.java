package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddQuestionActivityTest {

    private static final int READ_REQUEST_CODE = 42;

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class);

    @Before
    public void initiateIntents() {
        QuestionDatabase.get(mActivityRule.getActivity()).clearAllTables();
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testCheckOfTrueFalse() {
        onView(withId(R.id.true_false_radio)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(withText(R.string.truth_value)));
        onView(withId(R.id.radio_answer2)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(withText(R.string.false_value)));
        onView(withId(R.id.radio_answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.radio_answer4)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testCheckOfMCQ() {
        onView(withId(R.id.mcq_radio)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer4)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(isDisplayed()));
        onView(withId(R.id.radio_answer3)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(isDisplayed()));
        onView(withId(R.id.radio_answer2)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(withText("2")));
        onView(withId(R.id.radio_answer1)).perform(ViewActions.click()).check(matches(isChecked())).check(matches(withText("1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrivialInstanceQuestion() {
        Uri nullUri = null;
        Question nullQ = new Question(nullUri, false, 0);
    }

    @Test
    public void testSimpleInstanceQuestionTrueFalse() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, true, 0);
        assert (simple.isTrueFalse());
        assert (simple.getAnswer() == 0);
        assert (simple.getQuestionUri().equals(fake));
    }

    @Test
    public void testSimpleInstanceQuestionMCQ() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, false, 2);
        assert (!simple.isTrueFalse());
        assert (simple.getAnswer() == 2);
        assert (simple.getQuestionUri().equals(fake));
    }

    @Test
    public void zperformSearchTest() {
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(ViewActions.click());
        Intent i = new Intent();
        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, i);
        Intents.intending(anyIntent()).respondWith(intentResult);
    }

    @Test
    public void activityResultTest() {
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK, i);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(withText(uri.toString())));
    }

    @Test
    public void activityResultFalseTest() {
        //The text should not change because the activity returned with a error code
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_CANCELED, i);
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(isDisplayed()));
    }

    @Test
    public void addQuestionTest() {
        //Question: MCQ, answer: 0
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.radio_answer1)).perform(ViewActions.click());
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK, i);
        onView(ViewMatchers.withId(R.id.addQuestionButton)).perform(ViewActions.click());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                assertEquals(0, questions.get(0).getAnswer());
                assertEquals(false, questions.get(0).isTrueFalse());
            }
        });
        Intents.intending(hasComponent(MainActivity.class.getName()));
    }
}