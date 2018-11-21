package ch.epfl.sweng.studyup.quests;

import java.util.Observer;

import ch.epfl.sweng.studyup.items.Items;

public interface SpecialQuest extends QuestObserver {
    /**
     * Show the item that get rewarded when completing the quest
     * @return The item that we will get when the quest is completed
     */
    public Items reward();

    /**
     * Get title of the special quest
     */
    public String getTitle();

    /**
     * This method should be called when the quest has just been completed
     */
    public void onComplete();

}
