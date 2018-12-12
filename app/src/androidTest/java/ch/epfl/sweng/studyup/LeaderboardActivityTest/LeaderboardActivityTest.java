package ch.epfl.sweng.studyup.LeaderboardActivityTest;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
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
import ch.epfl.sweng.studyup.player.LeaderboardActivity;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(JUnit4.class)
public class LeaderboardActivityTest {

    @Rule
    public final ActivityTestRule<LeaderboardActivity> mActivityRule =
            new ActivityTestRule<>(LeaderboardActivity.class);

    @BeforeClass
    public static void init() { GlobalAccessVariables.MOCK_ENABLED = true; }

    @AfterClass
    public static void clean() {
        GlobalAccessVariables.MOCK_ENABLED = false;
    }

    @Test
    public void testLeaderboardByXPRendersByDefault() {
        waitAndTag(2000, LeaderboardActivityTest.class.getSimpleName());
        /*
        When opening the activity for the first time,
        the XP leaderboard should be displayed by default.
         */
        onView(withId(R.id.leaderboard_by_xp_container)).check(matches(isDisplayed()));
        onView(withId(R.id.leaderboard_by_correct_answers_container)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testMockPlayerDataAppearsInXPLeaderboard() {
        waitAndTag(2000, LeaderboardActivityTest.class.getSimpleName());
        /*
        Check that Dante Alighieri, a mock player, appears in the ranks of
        the XP leaderboard.
         */
        onView(allOf(withText("Dante Alighieri"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testToggleLeaderboardMode() {
        onView(withId(R.id.toggle_rank_mode_correct_answers)).perform(click());
        waitAndTag(2000, LeaderboardActivityTest.class.getSimpleName());

        /*
        Check that the questions answered leaderboard is now displayed.
        The XP leaderboard should now be hidden.
         */
        onView(withId(R.id.leaderboard_by_xp_container)).check(matches(not(isDisplayed())));
        onView(withId(R.id.leaderboard_by_correct_answers_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testMockPlayerDataAppearsInCorrectAnswersLeaderboard() {
        onView(withId(R.id.toggle_rank_mode_xp)).perform(click());
        waitAndTag(2000, LeaderboardActivityTest.class.getSimpleName());
        /*
        Check that Francesco Petrarca, another mock player, appears in the ranks of
        the correct answers leaderboard.
         */
        onView(allOf(withText("Francesco Petrarca"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isCompletelyDisplayed()));
    }
}
