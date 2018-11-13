package ch.epfl.sweng.studyup;

import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toolbar;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.social.RankingsActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class NavigationTest{
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public final ActivityTestRule<CustomActivity> customActivityRule = new ActivityTestRule<>(CustomActivity.class);
    @Rule
    public final ActivityTestRule<QuestsActivityStudent> questsActivityRule = new ActivityTestRule<>(QuestsActivityStudent.class);
    @Rule
    public final ActivityTestRule<RankingsActivity> rankingsActivityRule = new ActivityTestRule<>(RankingsActivity.class);
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

    /*@Test
    public void navigationTopTest() {
        Toolbar bnv = mainActivityRule.getActivity().findViewById(R.id.toolbar);
        bnv.inflateMenu();
        navigationBottomBar(bnv, false, false, false, false, true);
    }*/


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
            bnv.setSelectedItemId(R.id.navigation_rankings);
            intended(hasComponent(RankingsActivity.class.getName()));
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
}
