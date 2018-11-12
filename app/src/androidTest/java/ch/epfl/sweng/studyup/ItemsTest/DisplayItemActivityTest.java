package ch.epfl.sweng.studyup.ItemsTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.DisplayItemActivity;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class DisplayItemActivityTest {
    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule =
            new ActivityTestRule<>(InventoryActivity.class, true , false);


    @Test
    public void useButtonConsumsItem() {
        Player.get().addItem(Items.XP_POTION);
        int exp = Player.get().getExperience();
        Intent i = new Intent();
        i.putExtra(DisplayItemActivity.class.getName(), Items.COIN_SACK_NAME);
        mActivityRule.launchActivity(i);
        onView(withId(R.id.use_button)).perform(click());
        assertEquals(exp + Utils.XP_STEP, Player.get().getExperience());
    }
}
