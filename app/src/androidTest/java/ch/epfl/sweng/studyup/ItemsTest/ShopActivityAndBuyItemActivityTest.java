package ch.epfl.sweng.studyup.ItemsTest;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ShopActivityAndBuyItemActivityTest {
    private ListView list;

    @Rule
    public final ActivityTestRule<ShopActivity> mActivityRule =
            new ActivityTestRule<>(ShopActivity.class);

    @Before
    public void init() {
        list = mActivityRule.getActivity().findViewById(R.id.list_view_shop);
        Intents.init();
    }

    @Test
    public void BuyTwoCoinSackAndPlusMinusButton() {
        Player.get().addCurrency(30, mActivityRule.getActivity());
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        for(int i = 0; i < 3; ++i) {
            onView(withId(R.id.plus_button)).perform(click());
        }
        onView(withId(R.id.minus_button)).perform(click());
        onView(withId(R.id.buy_button)).perform(click());
        assertEquals(0, Player.get().getCurrency());
        assertEquals(Arrays.asList(Items.COIN_SACK, Items.COIN_SACK, Items.COIN_SACK), Player.get().getItems());
    }

    @Test
    public void BuyWithNotEnoughMoneyDoesNotWorAndBackButton() {
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        onView(withId(R.id.plus_button)).perform(click());
        onView(withId(R.id.buy_button)).perform(click());
        onView(withId(R.id.back_button_display_item)).perform(click());
        assertEquals(0, Player.get().getItems().size());
        BottomNavigationView b = mActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar); //Check if back in shopActivity since bottom bar isn't displayed in buyItemActivity
        b.setSelectedItemId(R.id.navigation_inventory);
    }



    @After
    public void resetPlayer() {
        Intents.release();
        Player.get().resetPlayer();
    }

}
