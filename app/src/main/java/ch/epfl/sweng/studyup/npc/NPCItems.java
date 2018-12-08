package ch.epfl.sweng.studyup.npc;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.studyup.items.Items;

public class NPCItems extends NPC {
    private List<Items> items;

    public NPCItems(List<Items> items, String name, LatLng latLng, int image) {
        super(name, latLng, image);
        items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    @Override
    void onYesButton(Activity activity) {
        
    }
}
