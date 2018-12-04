package ch.epfl.sweng.studyup.specialQuest;

import android.content.Context;

import com.google.common.base.Optional;

import ch.epfl.sweng.studyup.utils.Constants.SpecialQuestUpdateFlag;

public interface SpecialQuestObserver {
    void update(SpecialQuestUpdateFlag updateFlag);
}
