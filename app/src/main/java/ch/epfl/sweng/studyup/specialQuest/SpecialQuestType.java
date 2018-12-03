package ch.epfl.sweng.studyup.specialQuest;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants.SpecialQuestUpdateFlag;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public enum SpecialQuestType {

    THREE_QUESTIONS (
        R.string.special_quest_three_questions_title,
        R.string.special_quest_three_questions_description,
        3,
        SpecialQuestUpdateFlag.ANSWERED_QUESTION
    ),
    FIVE_QUESTIONS (
        R.string.special_quest_five_questions_title,
        R.string.special_quest_five_questions_description,
        5,
        SpecialQuestUpdateFlag.ANSWERED_QUESTION
    ),
    LEVEL_UP_BONUS (
        R.string.special_quest_level_up_bonus_title,
        R.string.special_quest_level_up_bonus_description,
        1,
        SpecialQuestUpdateFlag.LEVEL_UP
    ),
    CREATIVE_USERNAME (
        R.string.special_quest_creative_username_title,
        R.string.special_quest_creative_username_description,
        1,
        SpecialQuestUpdateFlag.SET_USERNAME
    ),
    CONSISTENT_USE (
        R.string.special_quest_consistent_use_title,
        R.string.special_quest_consistent_use_description,
        3,
        SpecialQuestUpdateFlag.USER_LOGIN
    );

    private final int titleId;
    private final int descriptionId;
    private int goal;
    private SpecialQuestUpdateFlag updateFlag;

    SpecialQuestType(int titleId, int descriptionId, int goal, SpecialQuestUpdateFlag updateFlag) {
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.goal = goal;
        this.updateFlag = updateFlag;
    }

    public String getTitle() {
        return MOST_RECENT_ACTIVITY.getString(titleId);
    }

    public String getDescription() {
        return MOST_RECENT_ACTIVITY.getString(descriptionId);
    }

    public int getGoal() { return this.goal; }

    public SpecialQuestUpdateFlag getUpdateFlag() { return this.updateFlag; }
}
