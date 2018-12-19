package ch.epfl.sweng.studyup.specialQuest;

import ch.epfl.sweng.studyup.utils.Constants;

public interface SpecialQuestObservable {

    void notifySpecialQuestObservers(Constants.SpecialQuestUpdateFlag updateFlag);
}
