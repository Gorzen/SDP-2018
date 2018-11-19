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
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ADisplayQuestionActivityTest2 {
    private static final String questionUUID = "Fake UUID test Display";
    private ListView list;

    @Rule
    public final ActivityTestRule<QuestsActivityStudent> mActivityRule =
            new ActivityTestRule<>(QuestsActivityStudent.class, true, false);

    @BeforeClass
    public static void changeSciper(){
        Player.get().setSciperNum("100001");
    }

    @AfterClass
    public static void deleteQuestion(){
        Firestore.get().deleteQuestion(questionUUID);
        Player.get().resetPlayer();
    }

    @Before
    public void setup(){
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void AddQuestion(){
        Question q = new Question(questionUUID, "ADisplayQuestionActivityTest2", false, 0, Constants.Course.SWENG.name());
        Firestore.get().addQuestion(q);
        Utils.waitAndTag(5000, "DisplayQuestionActivityTest2");
        Player.get().setRole(Constants.Role.teacher);
    }

    @Test
    public void aLoadQuestions(){
        Firestore.get().loadQuestions(mActivityRule.getActivity().getApplicationContext());
        Utils.waitAndTag(3000, "DisplayQuestionActivityTest2");
    }

    @Test
    public void displayQuestionTest(){
        list = mActivityRule.getActivity().findViewById(R.id.listViewQuests);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });

        Utils.waitAndTag(2000, "DisplayQuestionActivityTest2");

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer2)).perform(click());
        onView(withId(R.id.answer3)).perform(click());
        onView(withId(R.id.answer4)).perform(click());

        onView(withId(R.id.answer1)).perform(click());
        onView(withId(R.id.answer_button)).perform(click());

        Utils.waitAndTag(1000, "DisplayQuestionActivityTest2");
    }

    @Test
    public void test(){
        assertEquals(1, 1);
    }
}
