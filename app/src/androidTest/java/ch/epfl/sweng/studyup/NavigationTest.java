package ch.epfl.sweng.studyup;

import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.QuestsActivity;
import ch.epfl.sweng.studyup.social.ChatActivity;
import ch.epfl.sweng.studyup.social.RankingsActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class NavigationTest{
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

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
        intended(hasComponent(QuestsActivity.class.getName()));
        b.setSelectedItemId(R.id.navigation_rankings);
        intended(hasComponent(RankingsActivity.class.getName()));
    }
}
