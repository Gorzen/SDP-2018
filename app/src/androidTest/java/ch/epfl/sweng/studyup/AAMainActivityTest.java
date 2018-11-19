package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.Constants.CURR_DISPLAY;
import static ch.epfl.sweng.studyup.utils.Constants.LEVEL_DISPLAY;
import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AAMainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    /**
     * Hardcode of the implementation of player's function: getExperience
     */
    public void currencyDisplayTest() {
        int currency = Player.get().getCurrency();
        final int currencyToAdd = 10;

        onView(withId(R.id.currText)).check(matches(withText(CURR_DISPLAY + Player.get().getCurrency())));

        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Player.get().addCurrency(currencyToAdd, mActivityRule.getActivity());
            }
        });

        Utils.waitAndTag(300, "MainActivityTestCurrencyDisplayTest");

        onView(withId(R.id.currText)).check(matches(withText(CURR_DISPLAY + Player.get().getCurrency())));
        assertEquals(currency + currencyToAdd, Player.get().getCurrency());
    }

    @Test
    public void checkPlayerProgressionDisplay() {
        Player.get().resetPlayer();

        final int numberOfPush = 5;
        assert (mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
        onView(withId(R.id.levelText)).check(matches(withText(LEVEL_DISPLAY + Player.get().getLevel())));
        for (int i = 0; i < numberOfPush; ++i) {
            mActivityRule.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Player.get().addExperience(Constants.XP_STEP, mActivityRule.getActivity());

                }
            });
            assert (mActivityRule.getActivity().levelProgress.getProgress() == Player.get().getLevelProgress());
            onView(withId(R.id.levelText)).check(matches(withText(LEVEL_DISPLAY + Player.get().getLevel())));
        }
    }

    @Test
    public void testInfoNoException() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
    }

    @Test
    public void testSettingsNoException() {
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}