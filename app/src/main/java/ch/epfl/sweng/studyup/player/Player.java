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
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestNQuestions;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestObservable;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestObserver;
import ch.epfl.sweng.studyup.utils.Constants;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_DESCRIPTION;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_GOAL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_ID;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_PROGRESS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS_TITLE;
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
public class Player implements SpecialQuestObservable {

    //Observe list for quests
    private List<SpecialQuestObserver> observers;

    private List<SpecialQuest> activeQuests;

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

    private List<Course> courses;

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
        courses = new ArrayList<>();
        courses.add(Course.SWENG);
        observers = new ArrayList<>();
        activeQuests = new ArrayList<>();
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
        courses = new ArrayList<>();
        courses.add(Course.SWENG);
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
        activeQuests = getQuestsFromMap((List<Map<String, String>>) getOrDefault(remotePlayerData, FB_SPECIALQUESTS, Constants.getDefaultFirebaseQuests()));

        //TODO: call the update method for quests

        List<String> defaultCourseList = new ArrayList<>();
        defaultCourseList.add(Course.SWENG.name());
        courses = getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES, defaultCourseList));

        Log.d(TAG, "Loaded courses: " + courses.toString());
    }

    private List<SpecialQuest> getQuestsFromMap(List<Map<String, String>> questsAsStrings) {
        if (questsAsStrings.isEmpty()) {
            return Constants.DefaultQuests();
        }
        List<SpecialQuest> questList = new ArrayList<>();
        for (Map<String, String> q: questsAsStrings) {
            String id = q.get(FB_SPECIALQUESTS_ID);
            String title = q.get(FB_SPECIALQUESTS_TITLE);
            String description = q.get(FB_SPECIALQUESTS_DESCRIPTION);
            int goal = Integer.parseInt(q.get(FB_SPECIALQUESTS_GOAL));
            double progress = Double.parseDouble(q.get(FB_SPECIALQUESTS_PROGRESS));
            int level = Integer.parseInt(q.get(FB_SPECIALQUESTS_LEVEL));
            Constants.SpecialQuestsType type = Constants.SpecialQuestsType.valueOf(id);

            switch (type){
                case NQUESTIONS:
                    SpecialQuestNQuestions newQuest = new SpecialQuestNQuestions(title, description, goal, level);
                    newQuest.setProgress(progress);
                    questList.add(newQuest);
                    break;
                default:
                    throw new IllegalArgumentException("Quest ID " + id + "not found: cannot create the new quest");
            }
        }
        return questList;
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

    public List<Course> getCourses() {
        return courses;
    }

    public List<SpecialQuest> getActiveQuests() { return new ArrayList<>(activeQuests); }

    public List<Map<String, String>> getActiveQuestsMap() {
        /*
         *String format:
         * -Id
         * -Title
         * -Description
         * -Goal
         * -Progress
         */
        List<Map<String, String>> questList = new ArrayList<>();
        for (SpecialQuest sq: activeQuests) {
            Map<String, String> quest = new HashMap<>();
            quest.put(FB_SPECIALQUESTS_ID, sq.getId().name());
            quest.put(FB_SPECIALQUESTS_TITLE, sq.getTitle());
            quest.put(FB_SPECIALQUESTS_DESCRIPTION, sq.getDescription());
            quest.put(FB_SPECIALQUESTS_GOAL, Integer.toString(sq.getGoal()));
            quest.put(FB_SPECIALQUESTS_PROGRESS, Double.toString(sq.getProgress()));
            quest.put(FB_SPECIALQUESTS_LEVEL, Integer.toString(sq.getLevel()));
            questList.add(quest);
        }
        return questList;
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

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    public void setActiveQuests(List<SpecialQuest> quests) {
        activeQuests = new ArrayList<>(quests);
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

    public void removeQuest(SpecialQuest q) {
        activeQuests.remove(q);
    }

    @Override
    public void addObserver(SpecialQuestObserver o) {
        if (!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void removeObserver(SpecialQuestObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Object param) {
        for (SpecialQuestObserver o: observers) {
            o.update(this, param);
        }
    }
}