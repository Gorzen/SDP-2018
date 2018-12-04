package ch.epfl.sweng.studyup.SpecialQuestsTest;

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
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestType;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
    public void basicDisplayTest() {

        Intent launchIntent = new Intent();
        /*
        Launch special quest view for first of Player's special quests.
        By default, this is the "Answer 3 questions" special quest.
         */
        launchIntent.putExtra(Constants.SPECIAL_QUEST_INDEX_KEY, 0);

        mActivityRule.launchActivity(launchIntent);

        Utils.waitAndTag(500, "Waiting for the activity to be launched");

        /*
        Test that the activity correctly displayed special quest name.
        This assumes English is the default language.
         */
        onView(withId(R.id.specialQuestTitle)).check(matches(withText(SpecialQuestType.THREE_QUESTIONS.getTitle())));
        onView(withId(R.id.specialQuestDescription)).check(matches(withText(SpecialQuestType.THREE_QUESTIONS.getDescription())));

        /*
        Check that progress bar is displayed.
         */
        onView(withId(R.id.specialQuestProgress)).check(matches(isDisplayed()));
    }

    @Test
    public void questCompleteDisplayTest() {

        SpecialQuest defaultQuest = Player.get().getSpecialQuests().get(0);
        /*
        Set special quest to complete.
         */
        defaultQuest.setCompletionCount(3);

        Intent launchIntent = new Intent();
        /*
        Launch special quest view for first of Player's special quests.
        By default, this is the "Answer 3 questions" special quest.
        This special quest should now be complete.
         */
        launchIntent.putExtra(Constants.SPECIAL_QUEST_INDEX_KEY, 0);

        mActivityRule.launchActivity(launchIntent);

        /*
        Should display message indicating special quest has been completed.
         */
        onView(withId(R.id.specialQuestCongrat)).check(matches(withText(R.string.congrat_text_special_quest)));
    }
}
