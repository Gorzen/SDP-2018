package ch.epfl.sweng.studyup.SpecialQuestsTest;

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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsAnything.anything;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.specialQuest.AvailableSpecialQuestsActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

@RunWith(JUnit4.class)
public class AvailableSpecialQuestsActivityTest {

    @Rule
    public final ActivityTestRule<AvailableSpecialQuestsActivity> mActivityRule =
            new ActivityTestRule<>(AvailableSpecialQuestsActivity.class);

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
    public void testForDefaultSpecialQuest() {
        /*
        Test that the default special quest appears under
        "your special quests".
         */
        onData(anything()).inAdapterView(withId(R.id.your_special_quests_list_view))
            .atPosition(0).check(matches(isDisplayed()));
    }

    @Test
    public void testForAtLeastOneAvailableSpecialQuest() {
        /*
        Test that there is at least one special quest available under
        "available special quests"
         */
        onData(anything()).inAdapterView(withId(R.id.available_special_quests_list_view))
                .atPosition(0).check(matches(isDisplayed()));
    }

    @Test
    public void testAvailableSpecialQuestClickRedirect() {
        /*
        Test that clicking on an available special quests redirects to
        SpecialQuestDisplayActivity.
         */
        onData(anything()).inAdapterView(withId(R.id.available_special_quests_list_view))
                .atPosition(0).perform(click());

        intended(hasComponent(SpecialQuestDisplayActivity.class.getName()));
    }
}