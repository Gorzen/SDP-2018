package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AADisplayQuestionActivityTest3 {
    private static final String questionUUID = "Fake UUID test Display";
    Question q = new Question(questionUUID, "ADisplayQuestionActivityTest2", false, 0, Constants.Course.SWENG.name());
    private ListView list;

    @Rule
    public final ActivityTestRule<QuestsActivityStudent> mActivityRule =
            new ActivityTestRule<>(QuestsActivityStudent.class);

    @AfterClass
    public static void resetPlayer(){
        Player.get().resetPlayer();
    }

/*
    @Test
    public void AAA(){

    }

    @Test
    public void AddQuestion(){
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");
        Player.get().setRole(Constants.Role.teacher);
    }

    @Test
    public void aLoadQuestions(){
        Firestore.get().loadQuestions(mActivityRule.getActivity().getApplicationContext());
        Utils.waitAndTag(3000, "DisplayQuestionActivityTest2");

        Intent goToQuestion = new Intent();
        goToQuestion.putExtra(DISPLAY_QUESTION_TITLE, q.getTitle());
        goToQuestion.putExtra(DISPLAY_QUESTION_ID, q.getQuestionId());
        goToQuestion.putExtra(DISPLAY_QUESTION_TRUE_FALSE, Boolean.toString(q.isTrueFalse()));
        goToQuestion.putExtra(DISPLAY_QUESTION_ANSWER, Integer.toString(q.getAnswer()));

        intent2 = goToQuestion;
    }*/

    @Test
    public void addQuestion() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final List<Question> ListElementsArrayList = new ArrayList<>();

                final ArrayAdapter<Question> adapter = new ArrayAdapter<>
                        (mActivityRule.getActivity(), android.R.layout.simple_list_item_1,
                                ListElementsArrayList);

                list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);
                list.setAdapter(adapter);

                ListElementsArrayList.add(q);
                adapter.notifyDataSetChanged();
            }
        });

        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, list.getAdapter().getItemId(0));
            }
        });

        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());
    }

    /*
    @Test
    public void displayQuestionTestIntent() {
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");
        Player.get().setRole(Constants.Role.teacher);

        Firestore.get().loadQuestions(mActivityRule.getActivity().getApplicationContext());
        Utils.waitAndTag(3000, "DisplayQuestionActivityTest2");

        //mActivityRule.launchActivity(getIntentForDisplayQuestion(mActivityRule.getActivity().getApplicationContext(), q));
        list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);


        assertFalse(list.getCount() == 0);

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, list.getAdapter().getItemId(0));
            }
        });

        Utils.waitAndTag(1000, "DisplayQuestionActivityTest2");

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());
    }*/

    @Test
    public void zzz() {

    }
}
