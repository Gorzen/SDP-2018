package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom; //Random int library

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.Utils.*;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FirestoreTest {
    private static final String TAG = FirestoreTest.class.getSimpleName();
    private static final int WAIT_TIME_MILLIS = 500;

    // "Truth values"
    // Existing user
    private static final int sciper = 123456;
    private static final int level = 42;
    private static final String section = "IN";
    private static final int xp = 4137;
    private static final String year = "BA1";

    private static Map<String, Object> dummy = null;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Put the "Truth values" into the dummy map
     */
    public static void resetDummy() {
        dummy = new HashMap<>();
        dummy.put(FB_FIRSTNAME, INITIAL_FIRSTNAME);
        dummy.put(FB_LASTNAME, INITIAL_LASTNAME);
        dummy.put(FB_LEVEL, level);
        dummy.put(FB_SECTION, section);
        dummy.put(FB_XP, xp);
        dummy.put(FB_YEAR, year);
        dummy.put(FB_CURRENCY, INITIAL_CURRENCY);
    }

    /**
     * Initialisation of Firestore data
     */
    @Before
    public void setup() {
        // Player
        Player.get().setSciper(sciper);
        Player.get().setFirstName(INITIAL_FIRSTNAME);
        Player.get().setLastName(INITIAL_LASTNAME);

        resetDummy();
    }

    /**
     * Cleanup of modified values
     */
    @After
    public void cleanupSciperOnDB() {
        resetDummy();

        Firestore.get().resetUserInfos(sciper, INITIAL_FIRSTNAME, INITIAL_LASTNAME);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Player.get().reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLowTest() {
        Firestore.get().getAndSetUserData(MIN_SCIPER - 1, INITIAL_FIRSTNAME, INITIAL_LASTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHighTest() {
        Firestore.get().getAndSetUserData(MAX_SCIPER + 1, INITIAL_FIRSTNAME, INITIAL_LASTNAME);

    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperNegativeTest() {
        Firestore.get().getAndSetUserData(-42, INITIAL_FIRSTNAME, INITIAL_LASTNAME);
    }


    /*
    @Test(expected = NullPointerException.class)
    public void documentOnDBButEmptyTest() {
        Firestore.get().getAndSetUserData(MIN_SCIPER+1, "testFirstName", "testLastName");
        waitAndTag(500, TAG);
    }*/

    @Test
    public void deleteUserTest() {
        Firestore.get().getAndSetUserData(MAX_SCIPER, "John", "Doe");
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Player.get().addExperience(XP_STEP);
        Firestore.get().deleteUserFromDatabase(MAX_SCIPER);
        waitAndTag(WAIT_TIME_MILLIS, TAG);
        Firestore.getData(MAX_SCIPER);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        resetDummy();
        for (Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert (dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }
    }
    
    public void addNewUserToDBTest() {
        Firestore.get().getAndSetUserData(MAX_SCIPER, "John", "Doe");
        waitAndTag(WAIT_TIME_MILLIS, TAG);
        Player.get().addCurrency(ThreadLocalRandom.current().nextInt(1, 1000 + 1));
        Player.get().addCurrency(ThreadLocalRandom.current().nextInt(1, 1000 + 1));
        Firestore.get().deleteUserFromDatabase(MAX_SCIPER);
        waitAndTag(WAIT_TIME_MILLIS, TAG);
        Firestore.get().getAndSetUserData(MAX_SCIPER, "John", "Doe");
        waitAndTag(WAIT_TIME_MILLIS, TAG);
        Firestore.getData(MAX_SCIPER);
        waitAndTag(WAIT_TIME_MILLIS, TAG);


        //Check if values are the initial ones
        assertEquals(INITIAL_CURRENCY, Player.get().getCurrency());
        assertEquals(INITIAL_XP, Player.get().getExperience());

        //Check if the names are the correct ones
        assertEquals("John", dbStaticInfo.get(FB_FIRSTNAME));
        assertEquals("Doe", dbStaticInfo.get(FB_LASTNAME));
    }

    @Test
    public void setInfosTest() {
        int randomCurr = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
        dummy.put(FB_CURRENCY, randomCurr);
        Firestore.get().setUserInfos(sciper, dummy);

        Firestore.getData(sciper);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        for (Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert (dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }

    }

    @Test
    public void
    resetUser() {
        Player.get().setSciper(sciper);
        Firestore.get().setUserData(FB_XP, INITIAL_XP + 1);
        waitAndTag(WAIT_TIME_MILLIS, TAG);
        Firestore.get().resetUserInfos(sciper, INITIAL_FIRSTNAME, INITIAL_LASTNAME);

        resetDummy();

        Firestore.getData(sciper);

        for (Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert (dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }
    }

    @Test
    public void addXpPlayerAndCurrencyTest() {
        Player.get().reset();
        assertEquals(INITIAL_XP, Player.get().getExperience());
        assertEquals(INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(INITIAL_CURRENCY, Player.get().getCurrency());

        assertEquals(0.0, Player.get().getLevelProgress(), 10e-6);

        Player.get().addExperience(XP_TO_LEVEL_UP / 2);
        assertEquals(INITIAL_LEVEL, Player.get().getLevel());
        assertEquals(XP_TO_LEVEL_UP / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addExperience(XP_TO_LEVEL_UP);
        assertEquals(INITIAL_LEVEL + 1, Player.get().getLevel());
        assertEquals(CURRENCY_PER_LEVEL, Player.get().getCurrency());
        assertEquals((XP_TO_LEVEL_UP * 3) / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addCurrency(100);
        assertEquals(CURRENCY_PER_LEVEL + 100, Player.get().getCurrency());
    }

    @Test
    public void updateLevelAndCurrencyPropagateToServer() {
        final int numberLevelToUpgrade = 5;
        final int testSciper1 = sciper;
        final String testFirstName1 = INITIAL_FIRSTNAME;
        final String testLastName1 = INITIAL_LASTNAME;
        Firestore.get().resetUserInfos(testSciper1, testFirstName1, testLastName1);

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Firestore.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);
        Player.get().addExperience(numberLevelToUpgrade * XP_TO_LEVEL_UP + XP_TO_LEVEL_UP / 2);

        //To over-write the local state
        Firestore.get().getAndSetUserData(testSciper1 + 1, testFirstName1 + "1", testLastName1 + "1");

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Firestore.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        assert (Player.get().getLevel() == INITIAL_LEVEL + numberLevelToUpgrade);
        assert (Player.get().getCurrency() == INITIAL_CURRENCY + CURRENCY_PER_LEVEL * numberLevelToUpgrade);
    }
}