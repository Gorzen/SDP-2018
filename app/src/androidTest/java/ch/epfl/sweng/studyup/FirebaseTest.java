package ch.epfl.sweng.studyup;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static ch.epfl.sweng.studyup.Player.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.Player.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.Player.INITIAL_XP;
import static ch.epfl.sweng.studyup.Player.XP_STEP;
import static ch.epfl.sweng.studyup.Utils.FB_CURRENCY;
import static ch.epfl.sweng.studyup.Utils.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LASTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LEVEL;
import static ch.epfl.sweng.studyup.Utils.FB_SCIPER;
import static ch.epfl.sweng.studyup.Utils.FB_SECTION;
import static ch.epfl.sweng.studyup.Utils.FB_TOKEN;
import static ch.epfl.sweng.studyup.Utils.FB_USERS;
import static ch.epfl.sweng.studyup.Utils.FB_XP;
import static ch.epfl.sweng.studyup.Utils.FB_YEAR;
import static ch.epfl.sweng.studyup.Utils.MAX_SCIPER;
import static ch.epfl.sweng.studyup.Utils.MIN_SCIPER;
import static ch.epfl.sweng.studyup.Utils.waitAndTag;
import static ch.epfl.sweng.studyup.Utils.dbStaticInfo;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FirebaseTest {
    private static final String TAG = FirebaseTest.class.getSimpleName();

    // "Truth values"
    // Existing user
    private static final int sciper = 123456;
    private static final String firstname = "Jean-Louis";
    private static final String lastname = "Reymond";
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
        dummy.put(FB_FIRSTNAME, firstname);
        dummy.put(FB_LASTNAME, lastname);
        dummy.put(FB_LEVEL, level);
        dummy.put(FB_SECTION, section);
        dummy.put(FB_XP, xp);
        dummy.put(FB_YEAR, year);
    }

    /**
     * Initialisation of Firebase data
     */
    @Before
    public void setup() {
        // Player
        Player.get().setSciper(sciper);
        Player.get().setName(firstname, lastname);

        resetData();
    }

    /**
     * Cleanup of modified values
     */
    @After
    public void cleanup() {
        resetData();

        Firebase.get().resetUserInfos(sciper, firstname, lastname);
        waitAndTag(500, TAG);

        Player.get().reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLowTest() {
        Firebase.get().getAndSetUserData(Utils.MIN_SCIPER - 1, firstname, lastname);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHighTest() {
        Firebase.get().getAndSetUserData(Utils.MAX_SCIPER+1, firstname, lastname);

    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperNegativeTest() {
        Firebase.get().getAndSetUserData(-42, firstname, lastname);
    }


    /*
    @Test(expected = NullPointerException.class)
    public void documentOnDBButEmptyTest() {
        Firebase.get().getAndSetUserData(MIN_SCIPER+1, "testFirstName", "testLastName");
        waitAndTag(500, TAG);
    }*/

    @Test
    public void deleteUserTest() {
        Firebase.get().getAndSetUserData(MAX_SCIPER, "John", "Doe");
        waitAndTag(500, TAG);

        Player.get().addExperience(XP_STEP);
        Firebase.get().deleteUserFromDatabase(MAX_SCIPER);
        waitAndTag(500, TAG);
        Firebase.getData(MAX_SCIPER);
        waitAndTag(500, TAG);

        resetData();
        for(Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert(dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }
    }

    @Test
    public void addNewUserToDBTest() {
        Firebase.get().deleteUserFromDatabase(MAX_SCIPER);
        Firebase.get().getAndSetUserData( MAX_SCIPER, "John", "Doe");

        Firebase.getData(MAX_SCIPER);
        waitAndTag(500, TAG);

        assertEquals("John", dbStaticInfo.get(FB_FIRSTNAME));
        assertEquals("Doe", dbStaticInfo.get(FB_LASTNAME));
    }

    @Test
    public void setInfosTest() {
        Firebase.get().setUserInfos(sciper, dummy);

        Firebase.getData(sciper);
        waitAndTag(1000, TAG);

        for(Map.Entry<String, Object> entry : dummy.entrySet()) {
            assert(dbStaticInfo.get(entry.getKey()) == entry.getValue());
        }

    }

    @Test
    public void
    resetUser() {
        Player.get().setSciper(sciper);
        Firebase.get().setUserData(FB_XP, INITIAL_XP+1);
        Firebase.get().resetUserInfos(sciper, firstname, lastname);

        dummy.put(FB_SCIPER, sciper);
        dummy.put(FB_FIRSTNAME, firstname);
        dummy.put(FB_LASTNAME, lastname);
        dummy.put(FB_CURRENCY, INITIAL_CURRENCY);
        dummy.put(FB_LEVEL, INITIAL_LEVEL);
        dummy.put(FB_XP, INITIAL_XP);
        dummy.put(FB_TOKEN, null);

        Firebase.getData(sciper);

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
        final String testFirstName1 = firstname;
        final String testLastName1 = lastname;
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
