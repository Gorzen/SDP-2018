package ch.epfl.sweng.studyup.ItemsTest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.DisplayItemActivity;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryActivityTest {
    ListView list;
    Player player;


    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule =
            new ActivityTestRule<>(InventoryActivity.class, true, false);

    @Before
    public void init() {
        player = Player.get();
        player.addItem(Items.XP_POTION);
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void A_gettingToDisplayItemActivity() {
        list = mActivityRule.getActivity().findViewById(R.id.listViewItems);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
    }

    @Test
    public void B_getItemsNameReturnsEmptyList() {
        player.consumeItem(Items.XP_POTION);
        ArrayList<String> itemsName = mActivityRule.getActivity().getItemsNames();
        assertEquals(0, itemsName.size());
    }


    @Test
    public void C_getItemsNameReturnsCorrectList() {
        player.addItem(Items.XP_POTION);
        player.addItem(Items.XP_POTION);
        player.addItem(Items.COIN_SACK);
        assertEquals(Arrays.asList(Items.XP_POTION, Items.XP_POTION, Items.COIN_SACK), player.getItems());
    }


}
