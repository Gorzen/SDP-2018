package ch.epfl.sweng.studyup.player;

import ch.epfl.sweng.studyup.firebase.Firestore;

import static ch.epfl.sweng.studyup.firebase.Firestore.userData;
import static ch.epfl.sweng.studyup.utils.Utils.*;

/**
 * Player
 *
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
    private boolean isTeacher;
    private int sciper;
    private int[] questionsCurr;
    private int[] questsCurr;
    private int[] questionsAcheived;
    private int[] questsAcheived;

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
    }

    public static Player get() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
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
    private void updateLevel() {
        int newLevel = experience / XP_TO_LEVEL_UP + 1;

        if (newLevel - level > 0) {
            currency += (newLevel - level) * CURRENCY_PER_LEVEL;
            putUserData(FB_CURRENCY, currency);
            putUserData(FB_LEVEL, newLevel);
            Firestore.get().setUserData(FB_CURRENCY, currency);
            Firestore.get().setUserData(FB_LEVEL, newLevel);
            level = newLevel;
        }
    }

    public void addCurrency(int curr) {
        currency += curr;
        putUserData(FB_CURRENCY, currency);
        Firestore.get().setUserData(FB_CURRENCY, currency);

    }

    public void addExperience(int xp) {
        experience += xp;
        putUserData(FB_XP, experience);
        Firestore.get().setUserData(FB_XP, experience);

        updateLevel();
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
        putUserData(FB_SCIPER, sciper);
        putUserData(FB_FIRSTNAME, firstName);
        putUserData(FB_LASTNAME, lastName);
    }

    /**
     * Method used to save the state contained in the userData attribute of the class Firestore in
     * the class Player
     * (currently only saving experience, currency, names and sciper)
     */
    public void updatePlayerData() {
        // int newExperience = Ints.checkedCast((Long) userData.get(FB_XP))
        // Keeping this in case we want to have number attribute and not strings

        experience = Integer.parseInt(userData.get(FB_XP).toString());
        currency = Integer.parseInt(userData.get(FB_CURRENCY).toString());
        firstName = userData.get(FB_FIRSTNAME).toString();
        lastName = userData.get(FB_LASTNAME).toString();
        sciper = Integer.parseInt(userData.get(FB_SCIPER).toString());

        updateLevel();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;

        putUserData(FB_FIRSTNAME, firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;

        putUserData(FB_LASTNAME, lastName);
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
        if(isTeacher) {
            putUserData(FB_ROLE, FB_ROLES_T);
        } else {
            putUserData(FB_ROLE, FB_ROLES_S);
        }
    }

    public boolean getRole() {
        return isTeacher;
    }
}
