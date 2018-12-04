package ch.epfl.sweng.studyup.DisplayQuestionActivityTest;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.questions.DisplayQuestionActivity.getIntentForDisplayQuestion;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

@SuppressWarnings("HardCodedStringLiteral")
public class CannotAnswerTwiceTest extends DisplayQuestionActivityTest{

    //@Test
    public void cannotAnswerTwice(){
        Question testQuestion = new Question("abc", "test", true, 0, Constants.Course.SWENG.name(), "en");
        Player player = Player.get();
        player.addAnsweredQuestion(testQuestion.getQuestionId(), true, 0);
        Firestore.get().updateRemotePlayerDataFromLocal();

        Intent i = getIntentForDisplayQuestion(InstrumentationRegistry.getTargetContext(), testQuestion);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.answer_button)).perform(click());
        onView(withId(R.id.answer1)).perform(click());
        Intents.intending(hasComponent(QuestsActivityStudent.class.getName()));
    }
}
