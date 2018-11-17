package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.player.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ShopActivityAndBuyItemActivityTest {
    private ShopActivity shopActivity;
    private ListView list;

    private final ActivityTestRule<ShopActivity> mActivityRule =
            new ActivityTestRule<>(ShopActivity.class);

    @Before
    public void init() {
        shopActivity = mActivityRule.getActivity();
        list = shopActivity.findViewById(R.id.list_view_shop);
    }

    @Test
    public void BuyTwoCoinSackAndPlusMinusButton() {
        Player.get().addCurrency(20, shopActivity);
        shopActivity.runOnUiThread(new Runnable() {
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
        assertEquals(Arrays.asList(Items.COIN_SACK, Items.COIN_SACK), Player.get().getItems());
    }

    @Test
    public void BuyWithNotEnoughMoneyDoesNotWork() {
        Player.get().addCurrency(20, shopActivity);
        shopActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.performItemClick(list.getAdapter().getView(0, null, null), 0, 0);
            }
        });
        onView(withId(R.id.plus_button)).perform(click());
        onView(withId(R.id.buy_button)).perform(click());
        assertEquals(0, Player.get().getItems().size());
    }

}
