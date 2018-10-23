package ch.epfl.sweng.studyup;

import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.social.ChatActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class NavigationTest{
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testToChatActivity() {
        BottomNavigationView b = mActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        b.setSelectedItemId(R.id.navigation_chat);
        intended(hasComponent(ChatActivity.class.getName()));
    }

    @Test
    public void testToMapActivity() {
        BottomNavigationView b = mActivityRule.getActivity().findViewById(R.id.bottomNavView_Bar);
        b.setSelectedItemId(R.id.navigation_chat);
        intended(hasComponent(ChatActivity.class.getName()));
    }
}
