package ch.epfl.sweng.studyup;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.question.AddQuestionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String TAG = MainActivityTest.class.getSimpleName();
    private static final UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

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
            onView(withId(R.id.currText)).check(matches(withText(Utils.CURR_DISPLAY+Player.get().getCurrency())));
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
        onView(withId(R.id.levelText)).check(matches(withText(Utils.LEVEL_DISPLAY+Player.get().getLevel())));
        for(int i = 0; i < numberOfPush; ++i) {
            onView(withId(R.id.xpButton)).perform(click());
            assert(mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
            onView(withId(R.id.levelText)).check(matches(withText(Utils.LEVEL_DISPLAY+Player.get().getLevel())));
        }
    }

    @Test
    public void initializationGps(){
        assertEquals(Utils.mainContext, mActivityRule.getActivity().getApplicationContext());
        assertNotNull(Utils.locationProviderClient);
    }

    @Test
    public void testToAddQuestionButton() {
        onView(withId(R.id.questionButton)).perform(click());
        intended(hasComponent(AddQuestionActivity.class.getName()));
    }

    @Test
    public void testToCustomActWithB1() {
        onView(withId(R.id.pic_btn)).perform(click());
        intended(hasComponent(CustomActivity.class.getName()));
    }

    @Test
    public void testToCustomActWithB2() {
        onView(withId(R.id.pic_btn2)).perform(click());
        intended(hasComponent(CustomActivity.class.getName()));
    }

     @Test
    public void testLogin() {
         onView(withId(R.id.loginButton)).perform(click());
         Utils.waitAndTag(1000, TAG);
     }

    @Test
    public void testOptionNoException() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}