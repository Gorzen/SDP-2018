package ch.epfl.sweng.studyup.specialQuest;

import android.content.Context;

public interface SpecialQuestObservable {

    void notifySpecialQuestObservers(Context context, SpecialQuestType specialQuestType);
}
