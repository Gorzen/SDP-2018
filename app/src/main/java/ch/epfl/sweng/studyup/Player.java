package ch.epfl.sweng.studyup;

public class Player {
    private int experience;
    private int level;
    public final static int XP_TO_LEVEL_UP = 100;

    public Player(){
        experience = 0;
        level = 0;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    private void updateLevel(){
        level = experience / XP_TO_LEVEL_UP;
    }

    public void addExperience(int xp){
        experience += xp;
        updateLevel();
    }

    public double getLevelProgress(){
        return (experience % XP_TO_LEVEL_UP) * 1.0 / XP_TO_LEVEL_UP;
    }
}
