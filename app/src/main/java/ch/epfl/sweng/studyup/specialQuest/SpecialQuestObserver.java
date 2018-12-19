package ch.epfl.sweng.studyup.specialQuest;

import ch.epfl.sweng.studyup.utils.Constants.SpecialQuestUpdateFlag;

public interface SpecialQuestObserver {
    void update(SpecialQuestUpdateFlag updateFlag);
}
