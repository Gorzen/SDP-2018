package ch.epfl.sweng.studyup.QuestsTest;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestNQuestions;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(JUnit4.class)
public class SpecialQuestDisplayActivityTest {

    @Rule
    public final ActivityTestRule<SpecialQuestDisplayActivity> mActivityRule =
            new ActivityTestRule<>(SpecialQuestDisplayActivity.class, true, false);

    @BeforeClass
    public static void init() {
        GlobalAccessVariables.MOCK_ENABLED = true;
    }

    @AfterClass
    public static void clean() {
        GlobalAccessVariables.MOCK_ENABLED = false;
    }

    @Before
    public void initIntents(){
        Intents.init();
    }

    @After
    public void closeIntents(){
        Intents.release();
    }

    @Test
    public void basicTest() {
        //Launch Activity with basic quest
        Intent launchIntent = new Intent();
        String title = "Test title";
        String desc = "This is a description";
        SpecialQuestNQuestions fakeQuest = new SpecialQuestNQuestions(title, desc, 10, 0);
        fakeQuest.setProgress(0.5);
        launchIntent.putExtra(Constants.SPECIAL_QUEST_KEY, fakeQuest);
        mActivityRule.launchActivity(launchIntent);

        Utils.waitAndTag(500, "Waiting for the activity to be launched");

        Intents.intended(hasComponent(SpecialQuestDisplayActivity.class.getName()));
        onView(withId(R.id.specialQuestTitle)).check(matches(withText(title)));
        onView(withId(R.id.specialQuestDescription)).check(matches(withText(desc)));
        onView(withId(R.id.specialQuestProgress)).check(matches(isDisplayed()));
    }
}
