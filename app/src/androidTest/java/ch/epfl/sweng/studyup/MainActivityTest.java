package ch.epfl.sweng.studyup;

import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initiateIntents(){
        Intents.init();
    }
    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void testCanGreetUsers() {
        onView(withId(R.id.helloWorld)).check(matches(withText("Hello World!")));
    }

    @Test
    /**
     * Hardcode of the implementation of player's function: getExperience
     */
    public void simpleUseOfAddXpButton() {
        int currExp = Player.get().getExperience();
        final int numberOfPush = 5;
        for(int i = 0; i < numberOfPush; ++i) {
            onView(withId(R.id.xpButton)).perform(click());
            assert Player.get().getExperience() == (currExp+(i+1)*Player.XP_STEP%Player.XP_TO_LEVEL_UP) / Player.XP_TO_LEVEL_UP:
                    "xpButton doesn't update player's xp as expected.";
        }
    }
    @Test
    public void checkPlayerProgressionDisplay() {
        final int numberOfPush = 5;
        assert mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress() : "\n" +
                "Player's level display mismatch actual progression.";
        for(int i = 0; i < numberOfPush; ++i) {
            onView(withId(R.id.xpButton)).perform(click());
            assert mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress() :"\n" +
                    "Player's level display isn't well updated when pushing button.";
        }
    }

     /*
    @Test
    public void testToAddQuestionButton() {
        onView(withId(R.id.questButton)).perform(click());
        intended(hasComponent(AddQuestionActivity.class.getName()));
    }*/

}