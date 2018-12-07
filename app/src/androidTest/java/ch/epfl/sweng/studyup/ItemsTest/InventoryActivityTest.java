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
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class InventoryActivityTest {
    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule =
            new ActivityTestRule<>(InventoryActivity.class, true, false);
    ListView list;

    @Before
    public void init() {
        Player.get().resetPlayer();
        Player.get().addItem(Items.XP_POTION);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void cleanUp(){
        Player.get().resetPlayer();
    }

    @Test
    public void useButtonConsumesItem() {
        list = mActivityRule.getActivity().findViewById(R.id.listViewItems);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        Utils.waitAndTag(1000, this.getClass().getSimpleName());
        onView(withId(R.id.use_button)).perform(click());

        assertEquals(XP_STEP, Player.get().getExperience());
        assertEquals(0, Player.get().getItems().size());
    }

    @Test
    public void getItemsNameReturnsEmptyList() {
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
        onView(withId(R.id.top_navigation_settings)).perform(click());
    }
}