package ch.epfl.sweng.studyup;

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

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.imagePathGetter.mockImagePathGetter;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddQuestionActivityTest {

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class, true , false);

    @Before
    public void enableMock() {
        Utils.isMockEnabled = true;
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void disableMock() {
        Utils.isMockEnabled = false;
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
        Question nullQ = new Question(null, false, 0);
    }

    @Test
    public void testSimpleInstanceQuestionTrueFalse() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, true, 0);
        assert (simple.isTrueFalseQuestion());
        assert (simple.getAnswer() == 0);
        assert (simple.getQuestionUri().equals(fake));
    }

    @Test
    public void testSimpleInstanceQuestionMCQ() {
        Uri fake = Uri.parse("studyup://fake/path");
        Question simple = new Question(fake, false, 2);
        assert (!simple.isTrueFalseQuestion());
        assert (simple.getAnswer() == 2);
        assert (simple.getQuestionUri().equals(fake));
    }

    @Test
    public void activityResultTest() {
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(withText(mockImagePathGetter.fakeUri.toString())));
    }

    //@Test
    public void addQuestionTest() {
        //Question: MCQ, answer: 0
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.radio_answer1)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.addQuestionButton)).perform(ViewActions.click());
        List<Question> list = QuestionParser.parseQuestions(mActivityRule.getActivity().getApplicationContext(), false);
        assertNotNull(list);
        ArrayList<Question> parsedList = new ArrayList<>(list);
        assertEquals(0, parsedList.get(0).getAnswer());
        assertEquals(false, parsedList.get(0).isTrueFalseQuestion());
        Intents.intending(hasComponent(MainActivity.class.getName()));
    }
}