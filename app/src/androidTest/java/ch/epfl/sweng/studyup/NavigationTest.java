package ch.epfl.sweng.studyup;

import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.social.ChatActivity;
import ch.epfl.sweng.studyup.social.RankingsActivity;
import ch.epfl.sweng.studyup.utils.Utils;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class NavigationTest{
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void runOnceBeforeClass() {
        Utils.isMockEnabled = true;
    }

    @AfterClass
    public static void runOnceAfterClass() {
        Utils.isMockEnabled = false;
    }

    @Before
    public void init(){
        Intents.init();
    }

    @After
    public void release(){
        Intents.release();
    }

    @Test
    public void testNavigationBottomBar(){
        BottomNavigationView b = mActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        b.setSelectedItemId(R.id.navigation_chat);
        intended(hasComponent(ChatActivity.class.getName()));
        b.setSelectedItemId(R.id.navigation_map);
        intended(hasComponent(MapsActivity.class.getName()));
        b.setSelectedItemId(R.id.navigation_quests_student);
        intended(hasComponent(QuestsActivityStudent.class.getName()));
        b.setSelectedItemId(R.id.navigation_rankings);
        intended(hasComponent(RankingsActivity.class.getName()));
    }
}
