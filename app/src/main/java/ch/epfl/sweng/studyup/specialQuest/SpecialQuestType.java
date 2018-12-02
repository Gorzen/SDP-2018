package ch.epfl.sweng.studyup.specialQuest;

import ch.epfl.sweng.studyup.R;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public enum SpecialQuestType {

    THREE_QUESTIONS (
        R.string.special_quest_three_questions_title,
        R.string.special_quest_three_questions_description,
        3
    );



    private final int titleId;
    private final int descriptionId;
    private int goal;

    SpecialQuestType(int titleId, int descriptionId, int goal) {
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.goal = goal;
    }

    public String getTitle() {
        return MOST_RECENT_ACTIVITY.getString(titleId);
    }

    public String getDescription() {
        return MOST_RECENT_ACTIVITY.getString(descriptionId);
    }

    public int getGoal() { return this.goal; }
}
