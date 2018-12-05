package ch.epfl.sweng.studyup;

import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.ShopActivity;
import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.player.ScheduleActivityStudent;
import ch.epfl.sweng.studyup.settings.SettingsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class NavigationTest {
    @Rule
    public final ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule<>(HomeActivity.class);

    @BeforeClass
    public static void runOnceBeforeClass() {

        MOCK_ENABLED = true;

    }

    @AfterClass
    public static void runOnceAfterClass() {
        MOCK_ENABLED = false;
    }

    @Before
    public void init() {
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void testNavigationBottomBar() {
        BottomNavigationView b = mActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        b.setSelectedItemId(R.id.navigation_inventory);
        intended(hasComponent(InventoryActivity.class.getName()));
        b.setSelectedItemId(R.id.navigation_map);
        intended(hasComponent(MapsActivity.class.getName()));
        b.setSelectedItemId(R.id.navigation_quests_student);
        intended(hasComponent(QuestsActivityStudent.class.getName()));
        b.setSelectedItemId(R.id.navigation_schedule);
        intended(hasComponent(ScheduleActivityStudent.class.getName()));
    }

    @Ignore
    public void navigationTopTest() {
        onView(withId(R.id.top_navigation_infos)).perform(click());
        onView(withText("Infos are coming soon")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.top_navigation_settings)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

}

/*import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.ShopActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NavigationTest{
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public final ActivityTestRule<CustomActivity> customActivityRule = new ActivityTestRule<>(CustomActivity.class);
    @Rule
    public final ActivityTestRule<QuestsActivityStudent> questsActivityRule = new ActivityTestRule<>(QuestsActivityStudent.class);
    @Rule
    public final ActivityTestRule<ShopActivity> shopActivityRule = new ActivityTestRule<>(ShopActivity.class);
    @Rule
    public final ActivityTestRule<MapsActivity> mapActivityRule = new ActivityTestRule<>(MapsActivity.class);
    @Rule
    public final ActivityTestRule<InventoryActivity> inventoryActivityRule = new ActivityTestRule<>(InventoryActivity.class);

    @Before
    public void init(){
        Intents.init();
    }

    @After
    public void release(){
        Intents.release();
    }


    @Test
    public void navigationBottomBarTestMain() {
        BottomNavigationView bnv = mainActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, true, false, false, false, false);
    }
    @Test
    public void navigationBottomBarTestCust() {
        BottomNavigationView bnv = customActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, false, false, false, false, false);
    }
    @Test
    public void navigationBottomBarTestQues() {
        BottomNavigationView bnv = questsActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, false, true, false, false, false);
    }
    @Test
    public void navigationBottomBarTestRank() {
        BottomNavigationView bnv = rankingsActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, false, false, true, false, false);
    }
    @Test
    public void navigationBottomBarTestMaps() {
        BottomNavigationView bnv = mapActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, false, false, false, true, false);
    }
    @Test
    public void navigationBottomBarTestIven() {
        BottomNavigationView bnv = inventoryActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        navigationBottomBar(bnv, false, false, false, false, true);
    }

    @Test
    public void navigationTopTest() throws InterruptedException {
        onView(withId(R.id.top_navigation_settings)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }


    private void navigationBottomBar(BottomNavigationView bnv, boolean a, boolean b, boolean c, boolean d, boolean e){
        if(!a) {
            bnv.setSelectedItemId(R.id.navigation_home);
            intended(hasComponent(MainActivity.class.getName()));
        }
        if(!b) {
            bnv.setSelectedItemId(R.id.navigation_quests_student);
            intended(hasComponent(QuestsActivityStudent.class.getName()));
        }
        if(!c) {
            bnv.setSelectedItemId(R.id.navigation_shop);
            intended(hasComponent(ShopActivity.class.getName()));
        }
        if(!d) {
            bnv.setSelectedItemId(R.id.navigation_map);
            intended(hasComponent(MapsActivity.class.getName()));
        }
        if(!e) {
            bnv.setSelectedItemId(R.id.navigation_inventory);
            intended(hasComponent(InventoryActivity.class.getName()));
        }
    }
}*/
