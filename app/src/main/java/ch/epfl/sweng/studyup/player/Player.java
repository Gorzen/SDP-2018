package ch.epfl.sweng.studyup.player;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;

import static ch.epfl.sweng.studyup.firebase.Firestore.userData;
import static ch.epfl.sweng.studyup.utils.Utils.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Utils.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Utils.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Utils.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Utils.FB_ROLE;
import static ch.epfl.sweng.studyup.utils.Utils.FB_ROLES_S;
import static ch.epfl.sweng.studyup.utils.Utils.FB_ROLES_T;
import static ch.epfl.sweng.studyup.utils.Utils.FB_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.FB_USERNAME;
import static ch.epfl.sweng.studyup.utils.Utils.FB_XP;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_USERNAME;
import static ch.epfl.sweng.studyup.utils.Utils.INITIAL_XP;
import static ch.epfl.sweng.studyup.utils.Utils.MAX_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.MIN_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.XP_TO_LEVEL_UP;
import static ch.epfl.sweng.studyup.utils.Utils.getItemsFromInt;
import static ch.epfl.sweng.studyup.utils.Utils.getItemsInt;
import static ch.epfl.sweng.studyup.utils.Utils.putUserData;

/**
 * Player
 * <p>
 * Used to store the Player's state and informations.
 */
public class Player {
    private static final String TAG = Player.class.getSimpleName();
    private static Player instance = null;
    private int experience;
    private int level;
    private int currency;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isTeacher;
    private int sciper;
    private int[] questionsCurr;
    private int[] questsCurr;
    private int[] questionsAcheived;
    private int[] questsAcheived;
    private List<Items> items;


    public static String room = "INN_3_26";

    /**
     * Constructor called before someone is login.
     */
    private Player() {
        experience = INITIAL_XP;
        currency = INITIAL_CURRENCY;
        level = INITIAL_LEVEL;
        sciper = INITIAL_SCIPER;
        firstName = INITIAL_FIRSTNAME;
        lastName = INITIAL_LASTNAME;
        username = INITIAL_USERNAME;
        items = new ArrayList<>();
    }

    public static Player get() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public List<Items> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public void addItem(Items item) {
        if(items.add(item)) {
            putUserData(FB_ITEMS, getItemsInt());
            Firestore.get().setUserData(FB_ITEMS, getItemsInt());
        }
    }

    public void consumeItem(Items item) {
        if (items.remove(item)) {
            item.consume();
            putUserData(FB_ITEMS, getItemsInt());
            Firestore.get().setUserData(FB_ITEMS, getItemsInt());
        }else{
            throw new IllegalArgumentException("The player does not have this item, could not find it.");
        }
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrency() {
        return currency;
    }

    /**
     * Method suppose that we can only gain experience.
     */
    private void updateLevel(Activity activity) {
        int newLevel = experience / XP_TO_LEVEL_UP + 1;

        if (newLevel - level > 0) {
            addCurrency((newLevel - level) * CURRENCY_PER_LEVEL, activity);
            putUserData(FB_CURRENCY, currency);
            putUserData(FB_LEVEL, newLevel);
            Firestore.get().setUserData(FB_CURRENCY, currency);
            Firestore.get().setUserData(FB_LEVEL, newLevel);
            level = newLevel;
        }
    }

    public void addCurrency(int curr, Activity activity) {
        currency += curr;

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateCurrDisplay();
        }

        putUserData(FB_CURRENCY, currency);
        Firestore.get().setUserData(FB_CURRENCY, currency);
    }

    public void addExperience(int xp, Activity activity) {
        experience += xp;
        updateLevel(activity);

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateXpAndLvlDisplay();
            ((MainActivity) activity).updateCurrDisplay();
            Log.i("Check", "Activity is " + activity.toString() + " " + ((MainActivity) activity).getLocalClassName());
        }

        putUserData(FB_XP, experience);
        Firestore.get().setUserData(FB_XP, experience);
    }

    public double getLevelProgress() {
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }

    /**
     * Changes the Player to the basic state, right after constructor.
     */
    public void reset() {
        instance = new Player();
        instance.setSciper(INITIAL_SCIPER);
        instance.setFirstName(FB_FIRSTNAME);
        instance.setLastName(FB_LASTNAME);
        instance.setUserName(INITIAL_USERNAME);
        items = new ArrayList<>();
        List<Integer> itemsInt = new ArrayList<>();
        putUserData(FB_SCIPER, sciper);
        putUserData(FB_FIRSTNAME, firstName);
        putUserData(FB_LASTNAME, lastName);
        putUserData(FB_ITEMS, itemsInt);
        if (isTeacher)
            putUserData(FB_ROLE, FB_ROLES_T);
        else
            putUserData(FB_ROLE, FB_ROLES_S);
    }

    /**
     * Method used to save the state contained in the userData attribute of the class Firestore in
     * the class Player
     */
    public void updatePlayerData(Activity activity) throws NullPointerException {
        // int newExperience = Ints.checkedCast((Long) userData.get(FB_XP))
        // Keeping this in case we want to have number attribute and not strings
        try {
            experience = Integer.parseInt(userData.get(FB_XP).toString());
            currency = Integer.parseInt(userData.get(FB_CURRENCY).toString());
            level = Integer.parseInt(userData.get(FB_LEVEL).toString());
            firstName = userData.get(FB_FIRSTNAME).toString();
            lastName = userData.get(FB_LASTNAME).toString();
            sciper = Integer.parseInt(userData.get(FB_SCIPER).toString());
            username = userData.get(FB_USERNAME).toString();
            items = getItemsFromInt((List<Long>) userData.get(FB_ITEMS));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;

        putUserData(FB_FIRSTNAME, firstName);
    }

    public void setUserName(String new_username) {
        username = new_username;
        putUserData(FB_USERNAME, username);
        Firestore.get().setUserData(FB_USERNAME, username);
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;

        putUserData(FB_LASTNAME, lastName);
    }

    public String getUserName() {
        return username;
    }

    public int getSciper() {
        //to remove later
        if (sciper < MIN_SCIPER || sciper > MAX_SCIPER) {
            return INITIAL_SCIPER;
        }
        return sciper;
    }

    public void setSciper(int sciper) {
        this.sciper = sciper;
        putUserData(FB_SCIPER, sciper);
    }

    public void setRole(boolean isTeacher) {
        this.isTeacher = isTeacher;
        if (isTeacher) {
            putUserData(FB_ROLE, FB_ROLES_T);
        } else {
            putUserData(FB_ROLE, FB_ROLES_S);
        }
    }

    public boolean getRole() {
        return isTeacher;
    }

    public String getCurrentRoom() {
        return room;
    }
}
