package ch.epfl.sweng.studyup.specialQuest;

import android.content.Context;

import ch.epfl.sweng.studyup.utils.Constants.SpecialQuestUpdateFlag;

public interface SpecialQuestObserver {
    void update(Context context, SpecialQuestUpdateFlag updateFlag);
}
