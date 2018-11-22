package ch.epfl.sweng.studyup.specialQuest;

import java.io.Serializable;

import ch.epfl.sweng.studyup.items.Items;

/**
 * Interface or SpecialQuest.
 * All special quests must implement at least this functionality.
 * Implements serializable so that it can be passed between Activities as an extra.
 */
public interface SpecialQuest extends SpecialQuestObserver, Serializable {
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
     * This method should be called when the quest has just been completed
     */
    public void onComplete();

}
