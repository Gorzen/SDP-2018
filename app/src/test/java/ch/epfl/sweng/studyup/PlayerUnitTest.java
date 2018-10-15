package ch.epfl.sweng.studyup;

import android.util.Log;

import org.junit.Test;

import static ch.epfl.sweng.studyup.Utils.waitAndTag;
import static org.junit.Assert.*;

public class PlayerUnitTest {
    private final String TAG = PlayerUnitTest.class.getSimpleName();

    @Test
    public void resetPlayer() {
        Player.get().reset();

        assertEquals(0, Player.get().getExperience());
        assertEquals(0, Player.get().getLevel());
        assertEquals(0, Player.get().getCurrency());
    }

    @Test
    public void updateLevelAndCurrencyPropagateToServer() {
        final int numberLevelToUpgrade = 5;
        final int testSciper1 = 100000;
        final String testFirstName1 = "testFirstName";
        final String testLastName1 = "testLastName";
        Firebase.get().resetUserInfos(testSciper1, testFirstName1, testLastName1);

        waitAndTag(500, TAG);

        Firebase.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);
        Player.get().addExperience(numberLevelToUpgrade*Player.XP_TO_LEVEL_UP + Player.XP_TO_LEVEL_UP/2);

        //To over-write the local state
        Firebase.get().getAndSetUserData(testSciper1+1, testFirstName1+"1", testLastName1+"1");

        waitAndTag(500, TAG);

        Firebase.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);

        waitAndTag(500, TAG);

        assert(Player.get().getLevel() == Player.INITIAL_LEVEL+numberLevelToUpgrade);
        assert(Player.get().getCurrency() == Player.INITIAL_CURRENCY+Player.CURRENCY_PER_LEVEL*numberLevelToUpgrade);
    }


}