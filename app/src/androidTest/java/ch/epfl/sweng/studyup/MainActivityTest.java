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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Player.get().reset();
        Firebase.get().getAndSetUserData(Player.get().getSciper(),
                Player.get().getFirstName(), Player.get().getLastName());
        try{Thread.sleep(500);} catch(InterruptedException e) {}
        final int numberOfPush = 5;
        assert(mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress() );
        for(int i = 0; i < numberOfPush; ++i) {
            onView(withId(R.id.xpButton)).perform(click());
            assert(mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
        }
    }

    @Test
    public void initializationGps(){
        assertEquals(Utils.mainContext, mActivityRule.getActivity().getApplicationContext());
        assertNotNull(Utils.locationProviderClient);
    }

     /*
    @Test
    public void testToAddQuestionButton() {
        onView(withId(R.id.questButton)).perform(click());
        intended(hasComponent(AddQuestionActivity.class.getName()));
    }*/

}