package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.util.Log;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.firebase.Firestore.userData;

public class Utils {
    //Filename of the cached entry for persist login
    public static final String PERSIST_LOGIN_FILENAME = "persist_login.txt";
    public static final int TIME_TO_WAIT_FOR_LOGIN = 500; //[ms]

    // Firestore entries
    public static final String FB_USERS = "users";
    public static final String FB_USERNAME = "username";
    public static final String FB_ITEMS = "items";
    public static final String FB_FIRSTNAME = "firstname";
    public static final String FB_LASTNAME = "lastname";
    public static final String FB_SCIPER = "sciper";
    public static final String FB_ROLE = "role";
    public static final String FB_ROLES_S = "student";  //Enum???
    public static final String FB_ROLES_T = "teacher";
    public static final String FB_XP = "xp";
    public static final String FB_CURRENCY = "currency";
    public static final String FB_LEVEL = "level";
    public static final String FB_SECTION = "section";
    public static final String FB_YEAR = "year";
    public static final String FB_TOKEN = "token";
    public static final String FB_QUESTIONS = "questions";
    public static final String FB_QUESTION_AUTHOR = "author";
    public static final String FB_QUESTION_TITLE = "title";
    public static final String FB_QUESTION_ANSWER = "answer";
    public static final String FB_QUESTION_TRUEFALSE = "trueFalse";
    public static final String FB_QUESTS = "quests";
    public static final Set<String> FB_ALL_ENTRIES = Sets.newHashSet(
            FB_USERS, FB_FIRSTNAME, FB_LASTNAME, FB_SCIPER, FB_ROLE, FB_XP, FB_CURRENCY,
            FB_LEVEL, FB_SECTION, FB_YEAR, FB_TOKEN, FB_QUESTIONS, FB_QUESTS, FB_USERNAME);

    /**
     * Constant of firebase (mostly testing purpose)
     *
     * Reserved sciper:
     * MIN_SCIPER, MAX_SCIPER, 123456: reserved to manipulate in tests
     * MIN_SCIPER + 1: user present in database but with empty document (not valid format)
     */
    public static final int MAX_SCIPER = 999999;
    public static final int MIN_SCIPER = 100000;
    public static final long LOCATION_REQ_INTERVAL = 5 * 1000;
    public static final long LOCATION_REQ_FASTEST_INTERVAL = 5 * 1000;
    public static final String CAMERA = "Camera";
    public static final String GALLERY = "Gallery";
    public static final String CANCEL = "Cancel";
    public static final String JUSTONCE = "JUST ONCE";


    // Constant for Firebase Storage
    public static final String question_images_directory_name = "question_images";
    public static final String profile_pictures_directory_name = "profile_pictures";

    // Map that links item id to activities
    //public static final Map<Integer, Class> idToAct;
    // Better in resources?
    public static final String LEVEL_DISPLAY = "LEVEL ";
    public static final String CURR_DISPLAY = "MONEY:\n";
    public static Map<String, Object> dbStaticInfo = null;
    public static LatLng position = null;
    public static FusedLocationProviderClient locationProviderClient = null;
    public static Activity mainActivity = null;
    //test purpose
    public static Boolean isMockEnabled = false;
    public static Location mockLoc = null;

    // Basic Player stats
    public static final int XP_TO_LEVEL_UP = 100;
    public static final int CURRENCY_PER_LEVEL = 10;
    public static final int XP_STEP = 10;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_CURRENCY = 0;
    public static final int INITIAL_LEVEL = 1;
    public static final int INITIAL_SCIPER = MIN_SCIPER;
    public static final String INITIAL_USERNAME = "Player";
    public static final String INITIAL_FIRSTNAME = "Jean-Louis";
    public static final String INITIAL_LASTNAME = "Réymond";

    //Navigation items indexes for smooth transitions
    public static final int MAIN_INDEX=0, QUESTS_INDEX_STUDENT =1, RANKINGS_INDEX=2, MAP_INDEX=3, INVENTORY_INDEX =4, DEFAULT_INDEX_STUDENT=MAIN_INDEX;
    public static final int ADD_QUESTION_INDEX=0, QUESTS_INDEX_TEACHER=1, DEFAULT_INDEX_TEACHER=ADD_QUESTION_INDEX;

    //private static final java.util.Collections Collections = ;

    /*
    static {
        Map<Integer, Class> tempMap = new HashMap<>();
        tempMap.put(R.id.navigation_home, MainActivity.class);
        tempMap.put(R.id.navigation_quests, QuestsActivity.class);
        tempMap.put(R.id.navigation_rankings, RankingsActivity.class);
        tempMap.put(R.id.navigation_map, MapsActivity.class);
        tempMap.put(R.id.navigation_chat, InventoryActivity.class);
        idToAct = Collections.unmodifiableMap(tempMap);
    }
    */

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag  error message if any
     */
    public static final void waitAndTag(int time, String tag) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.w(tag, "Test was interrupted: " + e.getMessage());
            return;
        }
    }

    /**
     * Put the given information in Firestore.userData
     */
    public static void putUserData(String key, Object value) {
        if (userData == null) {
            userData = new HashMap<>();
        }

        userData.put(key, value);
    }

    public static void killApp(String tag){
        try{
            Runtime.getRuntime().exec("adb shell pm clear ch.epfl.sweng.studyup\n");
        }catch (IOException e) {
            Log.d(tag, e.getMessage());
        }
    }
}

