package ch.epfl.sweng.studyup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Utils;
import okhttp3.internal.Util;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AAAEditQuestionActivityTest {
    private final String questionUUID = "Test floepfl";
    private Question q;
    private ListView list;

    @Rule
    public final ActivityTestRule<AddQuestionActivity> mActivityRule =
            new ActivityTestRule<>(AddQuestionActivity.class, true, false);


    @Before
    public void init() {
        GlobalAccessVariables.MOCK_ENABLED = true;
        Player.get().setRole(Constants.Role.teacher);
    }

    private void clickOnListViewItem() {

    }

    @Test
    public void editTrueFalseQuestionAnswer0to1() {
        mActivityRule.launchActivity(new Intent());

        AddQuestionActivity mActivity = mActivityRule.getActivity();
        onView(withId(R.id.radio_answer2)).perform(click());
        Utils.waitAndTag(500, "Hello");
        onView(withId(R.id.addQuestionButton)).perform(click());
        Question question = mActivity.getQuestion();
        assertEquals(1, question.getAnswer());
        assertEquals(true, question.isTrueFalse());



       /* parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(1, questions.get(0).getAnswer());
                    assertEquals(true, questions.get(0).isTrueFalse());
                }
            }
        });*/
    }

    /*@Test
    public void editTrueFalseQuestionAnswer1to0() {
        q = new Question(questionUUID, "True false test", true, 1, Constants.Course.SWENG.name());
       /* File questionFile = null;
        try {
            questionFile = new File(mActivityRule.getActivity().getApplicationContext().getFilesDir(), questionUUID + ".txt");
            FileWriter writer = new FileWriter(questionFile);
            String questionData = "Blabla";
            if (questionData.isEmpty()) return;
            writer.write(questionData);
            writer.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        FileStorage.uploadProblemImage(questionFile);

        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer1)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(0, questions.get(0).getAnswer());
                    assertEquals(true, questions.get(0).isTrueFalse());
                }
            }
        });
    }

    @Test
    public void editMCQQuestionAnswer0to1() {
        q = new Question(questionUUID, "True false test", false, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(1, questions.get(0).getAnswer());
                    assertEquals(false, questions.get(0).isTrueFalse());
                }
            }
        });
    }

    @Test
    public void editMCQQuestionAnswer1to2() {
        q = new Question(questionUUID, "True false test", false, 1, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(2, questions.get(0).getAnswer());
                    assertEquals(false, questions.get(0).isTrueFalse());
                }
            }
        });
    }

    @Test
    public void editMCQQuestionAnswer2to3() {
        q = new Question(questionUUID, "True false test", false, 2, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(3, questions.get(0).getAnswer());
                    assertEquals(false, questions.get(0).isTrueFalse());
                }
            }
        });
    }

    @Test
    public void editMCQQuestionAnswer3to0() {
        q = new Question(questionUUID, "True false test", false, 3, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.radio_answer2)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
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
    }

    @Test
    public void editMCQToTrueFalseQuestionAnswer3to0() {
        q = new Question(questionUUID, "True false test", false, 3, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.true_false_radio)).perform(click());
        onView(withId(R.id.radio_answer1)).perform(click());
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(0, questions.get(0).getAnswer());
                    assertEquals(true, questions.get(0).isTrueFalse());
                }
            }
        });
    }

  /*  @Test
    public void editTrueFalseQuestionAnswerImagedToTextBased() {
        q = new Question(questionUUID, "True false test", true, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, this.getClass().getName());
        clickOnListViewItem();
        onView(withId(R.id.text_radio_button)).perform(click());
        Utils.waitAndTag(1000, this.getClass().getName());
        EditText text = mActivityRule.getActivity().findViewById(R.id.questionText);
        text.setText("editTrueFals");
        onView(withId(R.id.addQuestionButton)).perform(click());
        Firestore.get().loadQuestions(mActivityRule.getActivity());
        LiveData<List<Question>> parsedList = QuestionParser.parseQuestionsLiveData(mActivityRule.getActivity().getApplicationContext());
        assertNotNull(parsedList);
        parsedList.observe(mActivityRule.getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                if (!questions.isEmpty()) {
                    assertEquals(1, questions.get(0).getAnswer());
                    assertEquals(true, questions.get(0).isTrueFalse());
                }
            }
        });
    }*/




}
