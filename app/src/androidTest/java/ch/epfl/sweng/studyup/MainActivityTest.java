package ch.epfl.sweng.studyup;
import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import com.kosalgeek.android.caching.FileCacher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import java.io.IOException;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private MainActivity activity;
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule
            .grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @BeforeClass
    public static void runOnceBeforeClass() {
        Player.get().initializeDefaultPlayerData();
        MOCK_ENABLED = true;
    }
    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }
    @Before
    public void initiateIntents() {
        Intents.init();
        activity = mActivityRule.getActivity();
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
        for (int i = 0; i < numberOfPush; ++i) {
            mActivityRule.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.addExpPlayer();
                }
            });
            assert Player.get().getExperience() == ((currExp + (i + 1) * XP_STEP) % XP_TO_LEVEL_UP) / XP_TO_LEVEL_UP :
                    "xpButton doesn't update player's xp as expected.";
        }
        Utils.waitAndTag(1000, "Main Activity Test");
        onView(withId(R.id.currText)).check(matches(withText(CURR_DISPLAY + Player.get().getCurrency())));
    }
    @Test
    public void checkPlayerProgressionDisplay() {
        Player.resetPlayer();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        final int numberOfPush = 5;
        assert (mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
        onView(withId(R.id.levelText)).check(matches(withText(LEVEL_DISPLAY + Player.get().getLevel())));
        for (int i = 0; i < numberOfPush; ++i) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.addExpPlayer();
                }
            });
            assert (mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
            onView(withId(R.id.levelText)).check(matches(withText(LEVEL_DISPLAY + Player.get().getLevel())));
        }
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
    public void testOptionNoException() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}