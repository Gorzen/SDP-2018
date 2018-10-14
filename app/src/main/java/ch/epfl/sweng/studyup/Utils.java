package ch.epfl.sweng.studyup;

import android.util.Log;

import com.google.common.collect.Sets;

import java.util.Set;

public class Utils {
    public enum SECTION_SHORT {
        IN,
        SC,
        SV,
        PH,
        CH,

    }

    public enum YEAR {
        BA1,
        BA2,
        BA3,
        BA4,
        BA5,
        BA6,
    }

    //Firebase entries
    public static final String FB_USERS = "users";
    public static final String FB_FIRSTNAME = "firstname";
    public static final String FB_LASTNAME = "lastname";
    public static final String FB_SCIPER = "sciper";
    public static final String FB_XP = "xp";
    public static final String FB_CURRENCY = "currency";
    public static final String FB_LEVEL = "level";
    public static final String FB_SECTION = "section";
    public static final String FB_YEAR = "year";
    public static final String FB_TOKEN = "token";
    public static final String FB_QUESTIONS = "questions";
    public static final String FB_QUESTS = "quests";
    public static final Set<String> FB_ALL_ENTRIES= Sets.newHashSet(
            FB_USERS, FB_FIRSTNAME, FB_LASTNAME, FB_SCIPER, FB_XP, FB_CURRENCY,
            FB_LEVEL, FB_SECTION, FB_YEAR, FB_TOKEN, FB_QUESTIONS, FB_QUESTS
    );

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag error message if any
     */
    public static final void waitAndTag(int time, String tag) {
        try{
            Thread.sleep(time);
        } catch(InterruptedException e) {
            Log.w(tag, "Test was interrupted: "+e.getMessage());
            return;
        }
    }

}