package ch.epfl.sweng.studyup.npc;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;

public class NPCSpecialQuest extends NPC {
    SpecialQuest specialQuest;

    public NPCSpecialQuest(SpecialQuest specialQuest, String name, LatLng latLng, int image) {
        super(name, latLng, image);
        this.specialQuest = specialQuest;
    }
}
