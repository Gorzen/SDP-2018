package ch.epfl.sweng.studyup.QuestionsTest;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionDatabase;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddQuestionActivityTest {
    private static final String TAG = AddQuestionActivityTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class, true, false);

    @BeforeClass
    public static void enableMock() {
        MOCK_ENABLED = true;
        Intents.init();
        Player.get().initializeDefaultPlayerData();
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

    @Before
    public void closeKeyboard() {
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void testCheckOfTrueFalse() {
        onView(ViewMatchers.withId(R.id.true_false_radio)).perform(click());
        onView(withId(R.id.mcq_radio)).perform(click());
        onView(withId(R.id.true_false_radio)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.radio_answer1)).perform(click()).check(matches(isChecked())).check(matches(withText(R.string.truth_value)));
        onView(withId(R.id.radio_answer2)).perform(click()).check(matches(isChecked())).check(matches(withText(R.string.false_value)));
        onView(withId(R.id.radio_answer3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.radio_answer4)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testCheckOfMCQ() {
        onView(withId(R.id.mcq_radio)).perform(click());
        onView(withId(R.id.radio_answer4)).perform(ViewActions.scrollTo(), click());
        onView(withId(R.id.radio_answer3)).perform(click());
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.radio_answer1)).perform(click()).check(matches(isChecked()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFalseInstanceQuestion() {
        Question nullQ = new Question("1", null, false, 0, Course.SWENG.name());
    }

    @Test
    public void testSimpleInstanceQuestionTrueFalse() {
        Question simple = new Question("1", "test2134", true, 0, Course.SWENG.name());
        assert (simple.isTrueFalse());
        assert (simple.getAnswer() == 0);
        assert (simple.getQuestionId().equals("1"));
        assert (simple.getTitle().equals("test2134"));
    }

    @Test
    public void testSimpleInstanceQuestionMCQ() {
        Question simple = new Question("4", "test", false, 2, Course.SWENG.name());
        assert (!simple.isTrueFalse());
        assert (simple.getAnswer() == 2);
        assert (simple.getTitle().equals("test"));
        assert (simple.getQuestionId().equals("4"));
    }

    @Test
    public void activityResultTest() {
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(click());
        onView(ViewMatchers.withId(R.id.image_radio_button)).perform(click());
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(click());
        Bitmap sampleImage = BitmapFactory.decodeResource(mActivityRule.getActivity().getResources(), R.drawable.user_init_pic);
        File bitmapFile = new File(InstrumentationRegistry.getInstrumentation().getTargetContext().getFilesDir().toString() + "testfile.png");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(bitmapFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
        sampleImage.compress(Bitmap.CompressFormat.PNG, 100, os);
        Intent result = new Intent();
        result.setData(Uri.fromFile(bitmapFile));
        mActivityRule.getActivity().onActivityResult(42, Activity.RESULT_OK, result);
        onView(ViewMatchers.withId(R.id.display_question_path)).check(matches(not(isDisplayed())));
        onView(ViewMatchers.withId(R.id.addQuestion_display_image)).check(matches(isDisplayed()));
    }

    @Test
    public void addQuestionTest() throws Throwable {
        //Question: MCQ, answer: 0
        onView(ViewMatchers.withId(R.id.mcq_radio)).perform(click());
        onView(withId(R.id.image_radio_button)).perform(click());
        onView(ViewMatchers.withId(R.id.radio_answer1)).perform(click());
        onView(ViewMatchers.withId(R.id.selectImageButton)).perform(ViewActions.scrollTo(), click());

        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText title = mActivityRule.getActivity().findViewById(R.id.questionTitle);
                title.setText("A Title");
            }
        });
        onView(withId(R.id.display_question_path)).check(matches(not(isDisplayed())));
        onView(ViewMatchers.withId(R.id.addQuestionButton)).perform(click());

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
                    assertEquals(0, questions.get(0).getAnswer());
                    assertEquals(false, questions.get(0).isTrueFalse());
                }
            }
        });
        Utils.waitAndTag(100, TAG);
    }

    @Test
    public void testImageBasedQuestionTest() {
        //image-based question
        onView(withId(R.id.image_radio_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.selectImageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.display_question_path)).check(matches(isDisplayed()));
        onView(withId(R.id.addQuestion_display_image)).check(matches(isDisplayed()));
        onView(withId(R.id.questionText)).check(matches(not(isDisplayed())));
        //text-based question
        onView(withId(R.id.text_radio_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.selectImageButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.display_question_path)).check(matches(not(isDisplayed())));
        onView(withId(R.id.addQuestion_display_image)).check(matches(not(isDisplayed())));
        onView(withId(R.id.questionText)).check(matches(isDisplayed()));
    }
}