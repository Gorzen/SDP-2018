package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;

import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_KEY;

public class NPCSpecialQuest extends NPC {
    SpecialQuest specialQuest;

    public NPCSpecialQuest(SpecialQuest specialQuest, String name, LatLng latLng, int image) {
        super(name, latLng, image, new ArrayList<Integer>());
        this.specialQuest = specialQuest;
    }

    @Override
    public void onYesButton(Activity activity) {
        activity.startActivity(new Intent(activity, SpecialQuestDisplayActivity.class).putExtra(SPECIAL_QUEST_KEY, specialQuest));
    }
}
