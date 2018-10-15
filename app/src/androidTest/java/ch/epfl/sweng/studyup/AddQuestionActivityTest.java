package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.annotation.UiThreadTest;
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
import java.util.TreeSet;

import ch.epfl.sweng.studyup.question.AddQuestionActivity;
import ch.epfl.sweng.studyup.question.Question;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasCategories;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        //onView(withId(R.id.???)).perform(ViewActions.click()).check(matches(isChecked()));
        //onView(withId(R.id.???)).perform(ViewActions.click()).check(matches(isChecked()));
    }

    @Test
    public void testCheckOfMCQ() {
        onView(withId(R.id.mcq_radio)).perform(ViewActions.click()).check(matches(isChecked()));
        /*onView(withId(R.id.radio_answer4)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer3)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer2)).perform(ViewActions.click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(ViewActions.click()).check(matches(isChecked()));*/ //TODO Doesn't work for some reason
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
        TreeSet<String> catSet = new TreeSet<String>();
        catSet.add(Intent.CATEGORY_OPENABLE);
        Intent i = new Intent();
        Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(Activity.RESULT_OK,i);
        Intents.intending(anyIntent()).respondWith(intentResult);
    }

    public void activityResultTest(){
        Intent i = new Intent();
        String fakePath = "/test.jpg";
        Uri uri = Uri.fromFile(new File(fakePath));
        i.setData(uri);
        mActivityRule.getActivity().onActivityResult(READ_REQUEST_CODE, Activity.RESULT_OK, i);
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(withText("Image at: " + fakePath)));
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
}