package ch.epfl.sweng.studyup.specialQuest;

import java.io.Serializable;
import java.util.List;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.utils.Constants;

/**
 * Interface or SpecialQuest.
 * All special quests must implement at least this functionality.
 * Implements serializable so that it can be passed between Activities as an extra.
 */
public interface SpecialQuest extends SpecialQuestObserver, Serializable {
//Getters
    /**
     * Show the item that get rewarded when completing the quest
     * @return The item that we will get when the quest is completed
     */
    public Items reward();

    /**
     * Get title of the special quest
     * @return the title
     */
    public String getTitle();

    /**
     * Get the description of the special quest
     * @return the description
     */
    public String getDescription();

    /**
     * Get the current progress of the special quest
     * @return the progress, as a value between 0 and 1
     */
    public double getProgress();

    /**
     * Get the target number of the quest
     * @return The number of things to be done before the quest is complete
     */
    public int getGoal();

    public Constants.SpecialQuestsType getId();

    public int getLevel();

//Setters
    /**
     * Set the goal of the Quests
     * @param goal the number of things to be done before the quest is complete
     */
    public void setGoal(int goal);

    /**
     * Set the progress of the quest, a double between 0 and 1
     * @param progress The actual progress
     */
    public void setProgress(double progress);

    /**
     * This method should be called when the quest has just been completed and perform the following:
     * -Remove itself from the observer list
     * -Remove itself to the active quest list
     * -Add the reward to the player
     */
    public void onComplete();

    @Override
    public boolean equals(Object o);

}
