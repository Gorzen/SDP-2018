package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
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

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddQuestionActivityTest {

    private static final int READ_REQUEST_CODE = 42;

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class);

    @Before
    public void initiateIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testCheckOfTrueFalse() {
        onView(withId(R.id.true_false_radio)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer2)).perform(ViewActions.click()).check(matches(isChecked()));
    }

    @Test
    public void testCheckOfMCQ() {
        onView(withId(R.id.mcq_radio)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer4)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer3)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer2)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(ViewActions.click()).check(matches(isChecked()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrivialInstanceQuestion() {
        Question nullQ = new Question(null, false, 0);
    }

    @Test
    public void testSimpleInstanceQuestionTrueFalse() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, true, 0);
        assert(simple.isTrueFalseQuestion());
        assert(simple.getAnswer() == 0);
        assert(simple.getQuestionUri().equals(fake));
    }

    @Test
    public void testSimpleInstanceQuestionMCQ() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, false, 2);
        assert(!simple.isTrueFalseQuestion());
        assert(simple.getAnswer() == 2);
        assert(simple.getQuestionUri().equals(fake));
    }

    @Test
    public void zperformSearchTest(){
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(ViewActions.click());
        Intent i = new Intent();
        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK,i);
        Intents.intending(anyIntent()).respondWith(intentResult);
    }

    @Test
    public void activityResultTest(){
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK, i);
        try {
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(withText(uri.toString())));
    }

    @Test
    public void activityResultFalseTest(){
        //The text should not change because the activity returned with a error code
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_CANCELED, i);
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(isDisplayed()));
    }

    @Test
    public void addQuestionTest(){
        //Question: MCQ, answer: 0
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.radio_answer1)).perform(ViewActions.click());
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK, i);
        onView(ViewMatchers.withId(R.id.addQuestionButton)).perform(ViewActions.click());
        List<Question> list = QuestionParser.parseQuestions(mActivityRule.getActivity().getApplicationContext(), false);
        assertNotNull(list);
        ArrayList<Question> parsedList = new ArrayList<>(list);
        assertEquals(0, parsedList.get(0).getAnswer());
        assertEquals(false, parsedList.get(0).isTrueFalseQuestion());
        Intents.intending(hasComponent(MainActivity.class.getName()));
    }
}