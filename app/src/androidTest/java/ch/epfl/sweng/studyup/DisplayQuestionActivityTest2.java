package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DisplayQuestionActivityTest2 {
    private String questionUUID = "Fake UUID test Display";
    private ListView list;

    @Rule
    public final ActivityTestRule<QuestsActivityStudent> mActivityRule =
            new ActivityTestRule<>(QuestsActivityStudent.class, true, false);

    @BeforeClass
    public static void init(){
        Intents.init();
    }

    @AfterClass
    public static void release(){
        Intents.release();
    }

    @Before
    public void addQuestion(){
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void deleteQuestion(){
        Firestore.get().deleteQuestion(questionUUID);
        Utils.waitAndTag(3000, "Test");
    }

    @Test
    public void displayQuestionTest(){
        Question q = new Question(questionUUID, "Teacher quests test", false, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(3000, "Test");

        Player.get().setRole(Constants.Role.teacher);
        Utils.waitAndTag(3000, "Test");
        Firestore.get().loadQuestions(mActivityRule.getActivity().getApplicationContext());
        Utils.waitAndTag(3000, "Test");

        list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());
    }
}
