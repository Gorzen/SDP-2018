package ch.epfl.sweng.studyup;

public class Player {
    private int experience;
    private int level;
    public static int xpToLevelUp = 100;

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
        level = experience / xpToLevelUp;
    }

    public void addExperience(int xp){
        experience += xp;
        updateLevel();
    }

    public double getLevelProgress(){
        return (experience % xpToLevelUp) * 1.0/xpToLevelUp;
    }
}
