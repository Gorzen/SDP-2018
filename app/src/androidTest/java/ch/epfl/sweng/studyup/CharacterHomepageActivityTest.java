package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CharacterHomepageActivityTest {
    @Rule
    public final ActivityTestRule<CharacterHomepageActivity> mActivityRule =
            new ActivityTestRule<>(CharacterHomepageActivity.class);

    @Test
    public void unlockScreen() {
        unlockScreen();
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
}
