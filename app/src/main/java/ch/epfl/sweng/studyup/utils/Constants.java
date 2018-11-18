package ch.epfl.sweng.studyup.utils;

import android.content.res.Resources;

import com.google.common.collect.Sets;
import java.util.Set;

import ch.epfl.sweng.studyup.R;

@SuppressWarnings("HardCodedStringLiteral")
public abstract class Constants {

    // Values associated with Firebase
    public static final String FB_USERS = "users";
    public static final String FB_USERNAME = "username";
    public static final String FB_ITEMS = "items";
    public static final String FB_FIRSTNAME = "firstname";
    public static final String FB_LASTNAME = "lastname";
    public static final String FB_SCIPER = "sciper";
    public static final String FB_ROLE = "role";
    public static final String FB_ROLE_S = "student";
    public static final String FB_ROLE_T = "teacher";
    public static final String FB_XP = "xp";
    public static final String FB_CURRENCY = "currency";
    public static final String FB_LEVEL = "level";
    public static final String FB_SECTION = "section";
    public static final String FB_YEAR = "year";
    public static final String FB_TOKEN = "token";
    public static final String FB_QUESTIONS = "questions";
    public static final String FB_QUESTION_AUTHOR = "author";
    public static final String FB_QUESTION_TITLE = "title";
    public static final String FB_QUESTION_ID = "title";
    public static final String FB_QUESTION_ANSWER = "answer";
    public static final String FB_QUESTION_TRUEFALSE = "trueFalse";
    public static final String FB_QUESTS = "quests";
    public static final String FB_COURSE = "course";
    public static final String FB_COURSES = "courses";
    public static final Set<String> FB_ALL_ENTRIES = Sets.newHashSet(
            FB_USERS, FB_FIRSTNAME, FB_LASTNAME, FB_SCIPER, FB_ROLE, FB_XP, FB_CURRENCY,
            FB_LEVEL, FB_SECTION, FB_YEAR, FB_TOKEN, FB_QUESTIONS, FB_QUESTS, FB_USERNAME, FB_ITEMS);


    // Values associated with Firebase storage
    public static final String QUESTION_IMAGES_DIRECTORY_NAME = "question_images";
    public static final String PROFILE_PICTURES_DIRECTORY_NAME = "profile_pictures";

    // Values associated with intent extras
    public static final String INTENT_ROLE_KEY = "INTENT_ROLE_KEY";

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

    // URLs and filenames used in app
    public static final String AUTH_SERVER_URL = "https://studyup-authenticate.herokuapp.com/getCode";
    public static final String AUTH_SERVER_TOKEN_URL = "https://studyup-authenticate.herokuapp.com/getToken?code=";
    public static final String TEQUILA_AUTH_URL = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo?access_token=";

    public static final String PERSIST_LOGIN_FILENAME = "persist_login.txt";

    // Timeout values
    public static final int TIME_TO_WAIT_FOR_LOGIN = 2000; //[ms]
    public static final int TIME_TO_WAIT_FOR_AUTO_LOGIN = 2000; //[ms]

    // Basic Player stats
    public static final int XP_TO_LEVEL_UP = 100;
    public static final int CURRENCY_PER_LEVEL = 10;
    public static final int XP_STEP = 10;
    public static final int INITIAL_XP = 0;
    public static final int INITIAL_CURRENCY = 0;
    public static final int INITIAL_LEVEL = 1;
    public static final String INITIAL_SCIPER = String.valueOf(MIN_SCIPER);
    public static final String INITIAL_USERNAME = "Player";
    public static final String INITIAL_FIRSTNAME = "Jean-Louis";
    public static final String INITIAL_LASTNAME = "Réymond";

    public static final String LEVEL_DISPLAY = Resources.getSystem().getString(R.string.text_level);
    public static final String CURR_DISPLAY = Resources.getSystem().getString(R.string.text_money) + "\n";

    // Navigation items indexes for smooth transitions
    public static final int MAIN_INDEX=0, QUESTS_INDEX_STUDENT =1, SHOP_INDEX=2, MAP_INDEX=3, INVENTORY_INDEX =4, DEFAULT_INDEX_STUDENT=MAIN_INDEX;
    public static final int ADD_QUESTION_INDEX=0, QUESTS_INDEX_TEACHER=1, DEFAULT_INDEX_TEACHER=ADD_QUESTION_INDEX;

    //Settings constants
    public static final String[] LANGUAGES = {"English", "Français"};
    // Enums for Role, Course
    public enum Role {
        student,
        teacher
    }

    public enum Course {
        SWENG,
        CALCULUS,
        ECOLOGY,
        MEDIEVEL_HISTORY
    }
}
