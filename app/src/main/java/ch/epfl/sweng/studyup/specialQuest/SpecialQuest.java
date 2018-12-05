package ch.epfl.sweng.studyup.specialQuest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.Locale;
import java.util.Random;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants.SpecialQuestUpdateFlag;

import static ch.epfl.sweng.studyup.utils.Constants.ENGLISH;
import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_ALERT_ENGLISH;
import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_ALERT_FRENCH;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public class SpecialQuest implements SpecialQuestObserver, Serializable {

    private SpecialQuestType specialQuestType;
    private Items reward;
    private int completionCount;

    public SpecialQuest(SpecialQuestType specialQuestType) {
        this.specialQuestType = specialQuestType;
        this.completionCount = 0;

        // All special quests reward a random item
        Random random = new Random();
        this.reward = Items.values()[random.nextInt(Items.values().length)];
    }

    public SpecialQuestType getSpecialQuestType() { return specialQuestType; }

    public int getCompletionCount() { return completionCount; }
    public double getProgress() { return (double)completionCount/(double)specialQuestType.getGoal(); }
    public Items getReward() { return reward; }

    public void setCompletionCount(int completionCount) { this.completionCount = completionCount; }

    /*
    Called when an update is dispatched to an observing special quest.
    If the update type matches the current special quest type,
    then the special quest completion count should be incremented.
     */
    public void update(SpecialQuestUpdateFlag updateFlag) {
        if (!updateFlag.equals(this.specialQuestType.getUpdateFlag()) || completionCount >= specialQuestType.getGoal()) {
            return;
        }

        completionCount++;
        Firestore.get().updateRemotePlayerDataFromLocal();

        if (completionCount == specialQuestType.getGoal()) {
            // Reached goal, reward player with item, display congratulations
            Player.get().addItem(reward);

            String alertMessage = Locale.getDefault().getDisplayLanguage().equals(ENGLISH) ?
                    SPECIAL_QUEST_ALERT_ENGLISH : SPECIAL_QUEST_ALERT_FRENCH;

            Toast.makeText(MOST_RECENT_ACTIVITY.getApplicationContext(), alertMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /*
    Special quests are equal if they have the same type.
    This is used for determining whether a player is enrolled in a give special quest type.
     */
    public boolean equals(Object compSpecialQuest) {
        return compSpecialQuest != null &&
                compSpecialQuest instanceof SpecialQuest &&
                this.getSpecialQuestType().equals(((SpecialQuest) compSpecialQuest).getSpecialQuestType());

    }
}
