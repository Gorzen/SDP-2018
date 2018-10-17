package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.auth.AuthenticationActivity;
import ch.epfl.sweng.studyup.player.Player;

@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest extends TestCase {
    @Rule
    public final ActivityTestRule<AuthenticationActivity> rule =
            new ActivityTestRule<>(AuthenticationActivity.class);
    @Test
    public void creatingActivitySetPlayer() {
        assert(Player.get().getFirstName().equals(Player.INITIAL_FIRSTNAME));
        assert(Player.get().getLastName().equals(Player.INITIAL_LASTNAME));
    }
}
