package ch.epfl.sweng.studyup.player;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestObservable;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestObserver;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestType;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ANSWERED_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_ENROLLED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_TEACHED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS;
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
import static ch.epfl.sweng.studyup.utils.Utils.getCourseListFromStringList;
import static ch.epfl.sweng.studyup.utils.Utils.getItemsFromString;
import static ch.epfl.sweng.studyup.utils.Utils.getOrDefault;
import static ch.epfl.sweng.studyup.utils.Utils.getSpecialQuestListFromMapList;

/**
 * Player
 * <p>
 * Used to store the Player's state and informations.
 */
public class Player implements SpecialQuestObservable {

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
    private List<WeekViewEvent> scheduleStudent;

    private List<SpecialQuest> specialQuests;

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
        specialQuests = new ArrayList<>();
        // By default every player has a "three questions" special quest
        specialQuests.add(new SpecialQuest(SpecialQuestType.THREE_QUESTIONS));
        coursesEnrolled = new ArrayList<>();
        coursesTeached = new ArrayList<>();
        coursesEnrolled.add(Course.SWENG);
        scheduleStudent = new ArrayList<>();
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
        scheduleStudent = new ArrayList<>();
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

        List<SpecialQuest> remoteSpecialQuests =
                getSpecialQuestListFromMapList((List<Map<String, String>>) remotePlayerData.get(FB_SPECIALQUESTS));
        if (remoteSpecialQuests != null) {
            /*
            If special quests data in firebase, populate Player specialQuests with this data.
            Otherwise, leave Player specialQuests as is (the default).
             */
            this.specialQuests = remoteSpecialQuests;
        }
        //TODO: call the update method for quests

        List<String> defaultCourseListEnrolled = new ArrayList<>();
        defaultCourseListEnrolled.add(Course.SWENG.name());
        coursesEnrolled = getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES_ENROLLED, defaultCourseListEnrolled));

        List<String> defaultCourseListTeached = new ArrayList<>();
        coursesTeached = getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES_TEACHED, defaultCourseListTeached));

        answeredQuestions = (Map<String, Boolean>) getOrDefault(remotePlayerData, FB_ANSWERED_QUESTIONS, new HashMap<>());

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
    public List<WeekViewEvent> getScheduleStudent() {
        return scheduleStudent;
    }
    public Map<String, Boolean> getAnsweredQuestion() { return Collections.unmodifiableMap(new HashMap<>(answeredQuestions)); }


    public List<SpecialQuest> getSpecialQuests() { return specialQuests; }

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

        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).updateXpAndLvlDisplay();
            ((HomeActivity) activity).updateCurrDisplay();
            Log.i("Check", "Activity is " + activity.toString() + " " + ((HomeActivity) activity).getLocalClassName());
        }

        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void addCurrency(int curr, Activity activity) {
        currency += curr;

        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).updateCurrDisplay();
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

    /**
     * Set the given courses as this player's course (depending on the role). When setting some
     * teacher's courses, it will upload the courses' data on the server accordingly (overriding
     * the schedule of the course if someone else was teaching that course).
     *
     * @param courses The courses the player attends/teaches
     */
    public void setCourses(List<Course> courses) {
        if(role == Role.student) {
            this.coursesEnrolled = courses;
        } else {
            this.coursesTeached= courses;
            for(Course c : courses) {
                Firestore.get().setCourseTeacher(c);
            }
        }

        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void setScheduleStudent(List<WeekViewEvent> scheduleStudent) {
        this.scheduleStudent = scheduleStudent;
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

    @Override
    public void notifySpecialQuestObservers(Context context, SpecialQuestType specialQuestType) {
        for (SpecialQuestObserver specialQuest : specialQuests) {
            specialQuest.update(context, specialQuestType);
        }
    }
}