package ch.epfl.sweng.studyup;

public class Player {
    private static Player instance = null;
    private int experience;
    private int level;
    private int currency;
    public final static int XP_TO_LEVEL_UP = 100;
    public final static int CURRENCY_PER_LEVEL = 10;

    private Player(){
        experience = 0;
        level = 0;
        currency = 0;
    }

    public static Player get(){
        if(instance == null){
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

    public int getCurrency() { return currency; }

    private void updateLevel() {
        int newLevel = experience / XP_TO_LEVEL_UP;

        if(newLevel - level > 0){
            currency += (newLevel - level) * CURRENCY_PER_LEVEL;
        }

        level = newLevel;
    }

    public void addCurrency(int curr){
        currency += curr;
    }

    public void addExperience(int xp){
        experience += xp;
        updateLevel();
    }

    public double getLevelProgress(){
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }

    //Changes the Player to the basic state, right after constructor
    public void reset(){
        instance = new Player();
    }
}
