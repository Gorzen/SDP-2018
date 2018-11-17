package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.items.ShopActivity;



@RunWith(AndroidJUnit4.class)
public class ShopActivityAndBuyItemActivityTest {
    private ShopActivity shopActivity;

    private final ActivityTestRule<ShopActivity> mActivityRule =
            new ActivityTestRule<>(ShopActivity.class);

    @Before
    public void init() {
        shopActivity = mActivityRule.getActivity();
    }

    @Test
    public void onPlusButtonIncrementsCount() {

    }
}
