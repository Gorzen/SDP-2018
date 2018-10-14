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
    public void addXpPlayerAndCurrencyTest(){
        Player.get().reset();
        assertEquals(Player.INITIAL_XP, Player.get().getExperience());
        assertEquals(Player.INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(Player.INITIAL_CURRENCY, Player.get().getCurrency());

        assertEquals(0.0, Player.get().getLevelProgress(), 10e-6);

        Player.get().addExperience(Player.XP_TO_LEVEL_UP/2);
        assertEquals(Player.INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(Player.XP_TO_LEVEL_UP/2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addExperience(Player.XP_TO_LEVEL_UP);
        assertEquals(Player.INITIAL_LEVEL+1, Player.get().getLevel());
        assertEquals(Player.CURRENCY_PER_LEVEL, Player.get().getCurrency());
        assertEquals((Player.XP_TO_LEVEL_UP * 3) / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addCurrency(100);
        assertEquals(Player.CURRENCY_PER_LEVEL + 100, Player.get().getCurrency());
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