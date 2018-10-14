package ch.epfl.sweng.studyup;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static ch.epfl.sweng.studyup.Player.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.Player.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.Player.INITIAL_XP;
import static ch.epfl.sweng.studyup.Utils.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import static ch.epfl.sweng.studyup.Utils.FB_USERS;

/**
 * Bunch of tests for the Firebase DB unit.
 *
 * Sciper 100000: Defined as an empty user (HARDCODED)
 * Sciper 123456: Defined as an existing user (HARDCODED)
 * Sciper 999999: Defined as a "on-demand" user (HARDCODED)
 */
public class FirebaseUnitTest {
    // Initialization of the DB
    private static final String TAG = Firebase.class.getSimpleName();
    private static FirebaseFirestore db;

    // "Truth values"
    // Existing user
    private static final int sciper = 123456;
    private static final String firstname = "Jean-Louis";
    private static final String lastname = "Reymond";
    private static final int level = 42;
    private static final String section = "IN";
    private static final int xp = 1337;
    private static final String year = "BA1";

    private static Map<String, Object> dummy, onDB;

    /**
     * Retrieve the current state on the DB.
     *
     * This function put the current data of a given user into the onDB object.
     */
    public static void getData() {
        db.document(FB_USERS + "/" + Integer.toString(sciper))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if(document.exists()) {
                            onDB = document.getData();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error: getData" + sciper);
                    }
                });

        waitAndTag(500, TAG);
    }

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

        waitAndTag(500, TAG);
    }

    /**
     * Initialisation of Firebase data
     */
    @Before
    public void setup() {
        // Player
        Player.get().setSciper(sciper);
        Player.get().setName(firstname, lastname);

        // Firebase
        db = FirebaseFirestore.getInstance();

        // DB settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        resetData();
    }

    /**
     * Cleanup of modified values
     */
    @After
    public void cleanup() {
        resetData();

        db.document(FB_USERS+"/"+Integer.toString(sciper))
                .set(dummy)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Succes: setUserData(" + Player.get().getSciper() + ").");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error: setUserData, " + e.toString());
                    }
                });

        Player.get().reset();
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHighTest() {
        Firebase.get().getAndSetUserData(0, "testFirstName", "testLastName");

    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLowTest() {
        Firebase.get().getAndSetUserData(999999999, "testFirstName", "testLastName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperNegativeTest() {
        Firebase.get().getAndSetUserData(-42, "testFirstName", "testLastName");
    }

    @Test(expected = NullPointerException.class)
    public void documentOnDBButEmptyTest() {
        Firebase.get().getAndSetUserData(100000, "testFirstName", "testLastName");
    }

    @Test
    public void addNewUserToDBTest() {
        Firebase.get().getAndSetUserData(999999, "John", "Doe");

        getData();

        assertEquals("John", onDB.get(FB_FIRSTNAME));
        assertEquals("Doe", onDB.get(FB_LASTNAME));
    }

    @Test
    public void setInfosTest() {
        Firebase.get().setUserInfos(sciper, dummy);

        getData();

        assertEquals(dummy, onDB);
    }

    @Test
    public void resetUser() {
        Firebase.get().resetUserInfos(sciper, firstname, lastname);

        dummy.put(FB_SCIPER, sciper);
        dummy.put(FB_FIRSTNAME, firstname);
        dummy.put(FB_LASTNAME, lastname);
        dummy.put(FB_CURRENCY, INITIAL_CURRENCY);
        dummy.put(FB_LEVEL, INITIAL_LEVEL);
        dummy.put(FB_XP, INITIAL_XP);
        dummy.put(FB_TOKEN, null);

        getData();

        assertEquals(dummy, onDB);
    }
}
