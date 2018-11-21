package ch.epfl.sweng.studyup.player;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_ENROLLED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_TEACHED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_XP;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_USERNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_XP;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.Constants.XP_TO_LEVEL_UP;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.ROOM_NUM;
import static ch.epfl.sweng.studyup.utils.Utils.getCourseListFromStringList;
import static ch.epfl.sweng.studyup.utils.Utils.getItemsFromString;
import static ch.epfl.sweng.studyup.utils.Utils.getOrDefault;

/**
 * Player
 * <p>
 * Used to store the Player's state and informations.
 */
public class Player {

    private static final String TAG = Player.class.getSimpleName();

    private static Player instance = null;

    // Basic biographical data
    private String sciperNum;
    private String firstName;
    private String lastName;

    private String username;
    private Role role = null;

    // Game-related data
    private int experience;
    private int level;
    private int currency;
    private Map<String, Boolean> answeredQuestions;
    private List<Items> items;

    private List<Course> coursesEnrolled;
    private List<Course> coursesTeached;

    private Player() {
        sciperNum = INITIAL_SCIPER;
        firstName = INITIAL_FIRSTNAME;
        lastName = INITIAL_LASTNAME;
        experience = INITIAL_XP;
        currency = INITIAL_CURRENCY;
        level = INITIAL_LEVEL;
        username = INITIAL_USERNAME;
        answeredQuestions = new HashMap<>();
        items = new ArrayList<>();
        coursesEnrolled = new ArrayList<>();
        coursesTeached = new ArrayList<>();
        coursesEnrolled.add(Course.SWENG);
    }

    public static Player get() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    /**
     * User for testing purposes.
     * Clear data from current Player instance.
     */
    public void resetPlayer() {
        experience = INITIAL_XP;
        currency = INITIAL_CURRENCY;
        level = INITIAL_LEVEL;
        username = INITIAL_USERNAME;
        answeredQuestions = new HashMap<>();
        items = new ArrayList<>();
        coursesEnrolled = new ArrayList<>();
        coursesEnrolled.add(Course.SWENG);
    }

    /**
     * Populates the player class with persisted data.
     * This method is called from FireStore.loadPlayerData(), which is called
     * in AuthenticationActivity.
     */
    @SuppressWarnings("unchecked")
    public void updateLocalDataFromRemote(Map<String, Object> remotePlayerData) {

        if (remotePlayerData.isEmpty()) {
            Log.e(TAG,"Unable to retrieve player data from Firebase.");
            return;
        }

        username = getOrDefault(remotePlayerData, FB_USERNAME, INITIAL_USERNAME).toString();
        experience = Integer.parseInt(getOrDefault(remotePlayerData, FB_XP, INITIAL_XP).toString());
        currency = Integer.parseInt(getOrDefault(remotePlayerData, FB_CURRENCY, INITIAL_CURRENCY).toString());
        level = Integer.parseInt(getOrDefault(remotePlayerData, FB_LEVEL, INITIAL_LEVEL).toString());
        items = getItemsFromString((List<String>) getOrDefault(remotePlayerData, FB_ITEMS, new ArrayList<String>()));

        List<String> defaultCourseListEnrolled = new ArrayList<>();
        defaultCourseListEnrolled.add(Course.SWENG.name());
        coursesEnrolled = getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES_ENROLLED, defaultCourseListEnrolled));

        List<String> defaultCourseListTeached = new ArrayList<>();
        coursesEnrolled = getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES_TEACHED, defaultCourseListTeached));


        Log.d(TAG, "Loaded courses: \n");
        Log.d(TAG, "Enrolled: "+coursesEnrolled.toString()+"\n");
        Log.d(TAG, "Teached: "+coursesTeached.toString()+"\n");
    }

    // Getters
    public String getSciperNum() { return this.sciperNum; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public Role getRole() { return this.role; }
    public String getUserName() { return this.username; }
    public int getExperience() { return this.experience; }
    public int getLevel() { return this.level; }
    public int getCurrency() { return this.currency; }
    public String getCurrentRoom() { return ROOM_NUM; }
    public List<Items> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }
    public List<String> getItemNames() {
        List<String> itemStringsList = new ArrayList<>();
        for (Items currItem : items) {
            itemStringsList.add(currItem.name());
        }
        return itemStringsList;
    }
    public double getLevelProgress() {
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }

    public List<Course> getCoursesEnrolled() {
        return coursesEnrolled;
    }
    public List<Course> getCoursesTeached() {
        return coursesTeached;
    }


    // Setters
    public void setSciperNum(String sciperNum) {
        this.sciperNum = sciperNum;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public void setUserName(String newUsername) {
        username = newUsername;
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    // Method suppose that we can only gain experience.
    private void updateLevel(Activity activity) {
        int newLevel = experience / XP_TO_LEVEL_UP + 1;

        if (newLevel - level > 0) {
            addCurrency((newLevel - level) * CURRENCY_PER_LEVEL, activity);
            level = newLevel;
        }

        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void addExperience(int xp, Activity activity) {
        experience += xp;
        updateLevel(activity);

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateXpAndLvlDisplay();
            ((MainActivity) activity).updateCurrDisplay();
            Log.i("Check", "Activity is " + activity.toString() + " " + ((MainActivity) activity).getLocalClassName());
        }

        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void addCurrency(int curr, Activity activity) {
        currency += curr;

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateCurrDisplay();
        }

        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void addItem(Items item) {
        if (items.add(item)) {
            Firestore.get().updateRemotePlayerDataFromLocal();
        }
    }

    public void consumeItem(Items item)  {
        items.remove(item);
        item.consume();
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void setCoursesEnrolled(List<Course> coursesEnrolled) {
        this.coursesEnrolled = coursesEnrolled;
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void setCoursesTeached(List<Course> coursesTeached) {
        this.coursesTeached= coursesTeached;
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    /**
     * Add the questionID to answered questions field in Firebase, mapped with the value of the answer.
     */
    public void addAnsweredQuestion(String questionID, boolean isAnswerGood) {
        if(this.answeredQuestions.get(questionID) == null) {
            this.answeredQuestions.put(questionID, isAnswerGood);
            Firestore.get().updateRemotePlayerDataFromLocal();
        }
    }

    public Map<String, Boolean> getAnsweredQuestion() {
        return this.answeredQuestions;
    }

    public boolean isDefault() throws NumberFormatException {

        /*
        If player has not yet been loaded,
        these values will be the default ones.
        This is used in AuthenticationActivity.
         */
        return this.firstName.equals(INITIAL_FIRSTNAME) &&
            this.lastName.equals(INITIAL_LASTNAME) &&
            this.sciperNum.equals(INITIAL_SCIPER);
    }
}