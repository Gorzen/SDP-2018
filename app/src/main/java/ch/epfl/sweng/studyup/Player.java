package ch.epfl.sweng.studyup;

import az.plainpie.PieView;

public class Player {
    private int experience;
    private int level;
    private int currency;
    public final static int XP_TO_LEVEL_UP = 100;
    private final static int CURRENCY_PER_LEVEL = 10;

    public Player(){
        experience = 0;
        level = 0;
        currency = 0;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrency() { return currency; }

    private void updateLevel() {
        level = experience / XP_TO_LEVEL_UP;
        currency += CURRENCY_PER_LEVEL;
    }

    public void addExperience(int xp){
        experience += xp;
        updateLevel();
    }

    public double getLevelProgress(){
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }
}
