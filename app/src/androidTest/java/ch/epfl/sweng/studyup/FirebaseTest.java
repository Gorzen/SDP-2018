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

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.player.Player.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.player.Player.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.player.Player.INITIAL_LASTNAME;
import static ch.epfl.sweng.studyup.player.Player.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.player.Player.INITIAL_XP;
import static ch.epfl.sweng.studyup.player.Player.XP_STEP;
import static ch.epfl.sweng.studyup.utils.Utils.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Utils.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Utils.FB_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.FB_SECTION;
import static ch.epfl.sweng.studyup.utils.Utils.FB_TOKEN;
import static ch.epfl.sweng.studyup.utils.Utils.FB_XP;
import static ch.epfl.sweng.studyup.utils.Utils.FB_YEAR;
import static ch.epfl.sweng.studyup.utils.Utils.MAX_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;
import static ch.epfl.sweng.studyup.utils.Utils.dbStaticInfo;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FirebaseTest {
    private static final String TAG = FirebaseTest.class.getSimpleName();
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
    public static void resetData() {
        dummy = new HashMap<>();
        dummy.put(FB_FIRSTNAME, INITIAL_FIRSTNAME);
        dummy.put(FB_LASTNAME, INITIAL_LASTNAME);
        dummy.put(FB_LEVEL, level);
        dummy.put(FB_SECTION, section);
        dummy.put(FB_XP, xp);
        dummy.put(FB_YEAR, year);
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

        resetData();
    }

    /**
     * Cleanup of modified values
     */
    @After
    public void cleanup() {
        resetData();

        Firestore.get().resetUserInfos(sciper, INITIAL_FIRSTNAME, INITIAL_LASTNAME);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Player.get().reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLowTest() {
        Firestore.get().getAndSetUserData(Utils.MIN_SCIPER - 1, INITIAL_FIRSTNAME, INITIAL_LASTNAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHighTest() {
        Firestore.get().getAndSetUserData(Utils.MAX_SCIPER+1, INITIAL_FIRSTNAME, INITIAL_LASTNAME);

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

        resetData();
        for(Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert(dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }
    }

    @Test
    public void addNewUserToDBTest() {
        Firestore.get().deleteUserFromDatabase(MAX_SCIPER);
        Firestore.get().getAndSetUserData( MAX_SCIPER, "John", "Doe");
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Firestore.getData(MAX_SCIPER);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        assertEquals("John", Player.get().getFirstName());
        assertEquals("Doe", dbStaticInfo.get(FB_LASTNAME));
    }

    @Test
    public void setInfosTest() {
        Firestore.get().setUserInfos(sciper, dummy);

        Firestore.getData(sciper);
        waitAndTag(WAIT_TIME_MILLIS, TAG);

        for(Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert(dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }

    }

    @Test
    public void
    resetUser() {
        Player.get().setSciper(sciper);
        Firestore.get().setUserData(FB_XP, INITIAL_XP+1);
        Firestore.get().resetUserInfos(sciper, INITIAL_FIRSTNAME, INITIAL_LASTNAME);

        dummy.put(FB_SCIPER, sciper);
        dummy.put(FB_FIRSTNAME, INITIAL_FIRSTNAME);
        dummy.put(FB_LASTNAME, INITIAL_LASTNAME);
        dummy.put(FB_CURRENCY, INITIAL_CURRENCY);
        dummy.put(FB_LEVEL, INITIAL_LEVEL);
        dummy.put(FB_XP, INITIAL_XP);
        dummy.put(FB_TOKEN, null);

        Firestore.getData(sciper);

        for(Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert(dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }
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
        final int testSciper1 = sciper;
        final String testFirstName1 = INITIAL_FIRSTNAME;
        final String testLastName1 = INITIAL_LASTNAME;
        Firestore.get().resetUserInfos(testSciper1, testFirstName1, testLastName1);

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Firestore.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);
        Player.get().addExperience(numberLevelToUpgrade*Player.XP_TO_LEVEL_UP + Player.XP_TO_LEVEL_UP/2);

        //To over-write the local state
        Firestore.get().getAndSetUserData(testSciper1+1, testFirstName1+"1", testLastName1+"1");

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        Firestore.get().getAndSetUserData(testSciper1, testFirstName1, testLastName1);

        waitAndTag(WAIT_TIME_MILLIS, TAG);

        assert(Player.get().getLevel() == Player.INITIAL_LEVEL+numberLevelToUpgrade);
        assert(Player.get().getCurrency() == Player.INITIAL_CURRENCY+Player.CURRENCY_PER_LEVEL*numberLevelToUpgrade);
    }
}