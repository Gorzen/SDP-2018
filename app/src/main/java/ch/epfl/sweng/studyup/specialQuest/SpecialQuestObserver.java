package ch.epfl.sweng.studyup.specialQuest;

import android.content.Context;

public interface SpecialQuestObserver {
    void update(Context context, SpecialQuestType specialQuestType);
}
