package ch.epfl.sweng.studyup.ItemsTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InventoryActivityTest {
    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule =
            new ActivityTestRule<>(InventoryActivity.class, true, false);
    ListView list;

    @Before
    public void init() {
        Player.get().addItem(Items.XP_POTION);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void cleanup() {
        Player.get().resetPlayer();
    }

    @Test
    public void useButtonConsumsItem() {
        list = mActivityRule.getActivity().findViewById(R.id.listViewItems);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        int exp = Player.get().getExperience();
        onView(withId(R.id.use_button)).perform(click());
        Utils.waitAndTag(500, "Wait to consume item");
        assertEquals(exp + XP_STEP, Player.get().getExperience());
    }

    @Test
    public void getItemsNameReturnsEmptyList() {
        Player player = Player.get();
        Player.get().consumeItem(Items.XP_POTION);
        ArrayList<String> itemsName = Items.getPlayersItemsNames();
        assertEquals(0, itemsName.size());
    }

    @Test
    public void getItemsNameReturnsCorrectList() {
        Player.get().addItem(Items.XP_POTION);
        Player.get().addItem(Items.XP_POTION);
        Player.get().addItem(Items.COIN_SACK);
        assertEquals(Arrays.asList(Items.XP_POTION, Items.XP_POTION, Items.XP_POTION, Items.COIN_SACK), Player.get().getItems());
    }

    @Test
    public void testOptionNoException() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}